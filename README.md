authentikat-jwt
===============

A simple scala-jwt library.
JWT - pronounced 'jot' - is a claims-based authentication standard. 

Motivation
----------

The motivation for this project is to eventually create an OSS Security Token Service for distributed systems.

Setup
=====

Authentikat-Jwt has been published to sonatype:

Add the resolver and the dependency


    resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

    libraryDependencies ++= Seq(
      "com.jason-goodwin" %% "authentikat-jwt" % "0.3.1"
    )

JWT - A Claims Based Authentication
===================================

Before we get into how to use the library, we'll explore a usage example to demonstrate how claims based authentication can be used.

Let's assume we have a secret key, and the key is "secret-key".
Let's start the usage example imagining we have an http based service or web application. We receive an http request to do something in our http server application with a header containing a JWT (pronounced "Jot")
We need to validate the request is authenticated by analyzing the JWT. First, let's look at the structure of a JWT.

A token consists of 3 dot separated values. The token our server receives:

    eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJIZXkiOiJmb28ifQ==.e89b48f1e2016b78b805c1430d5a139e62bba9237ff3e34bad56dae6499b2647
    ^JwtHeader                           ^JwtClaimsSet        ^JsonWebSignature - One way hash of the JwtClaimsSet

The JwtHeader and JwtClaimsSet are Base64Encoded Json - they can be decoded and used. The signiture is used to validate the claims were created by someone with the secret key and to ensure they have not been modified since issuing.

In the example above, the header and claims contain the following simple json:

    Header:
    {"alg":"HS256","typ":"JWT"}

    ClaimsSet:
    {"Hey":"foo"}

JwtHeader
---------

    Header:
    {"alg":"HS256","typ":"JWT"} + Base64Encoding = eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9

The first part of the token is the header.
The header contains info needed to understand the content of the key. 

- alg: It contains the algorith used for the Signature - in the example case, HMAC SHA-256 .
- typ: It contains an optional typ (mimeType) field - default containing "JWT" to indicate this is a Json Web Token.
- cty: It can also contain a content type ("cty") field, This field, if used, could indicate that the content is another token. (This feature is not currently supported.)

Currently supported encryption algorithms:
- "none" gives a blank signature. It could be used for passing data around in a small space such as a header.
- "HS256"
- "HS384"
- "HS512"

JwtClaimsSet
------------

    ClaimsSet:
    {"Hey":"foo"} + Base64Encoding = eyJIZXkiOiJmb28ifQ==

The second part of the token is the claims.
Jwt is claims based authentication. See: http://en.wikipedia.org/wiki/Claims-based_identity

The Claims Set can contain any number of public or private claims. Because the claims are used to create a signature with a secret key, they cannot be modified after the token is signed.

The optional public claims that the standard supports are defined in the JWT draft here: http://self-issued.info/docs/draft-ietf-oauth-json-web-token.html
They include items like Issuer (iss) that indicates who the token was created by. It can contain an Expiry (exp) to indicate when the token will expire. 

Private claims can be anything (but obviously should not duplicate keys in the public claims space). Consider data that might be used to draw user session info in a UI. Including a username in the private claims is useful for validating the token is being used by the appropriate user.

Signature
---------

    {"Hey":"foo"} + Base64Encoding + HmacSha256 = e89b48f1e2016b78b805c1430d5a139e62bba9237ff3e34bad56dae6499b2647

The third portion of the token is a one way hash of the Base64 encoded claims set.
In the example, it's a hash using a secret key "secretkey" of the string "eyJIZXkiOiJmb28ifQ==" using Hmac SHA256.
The result is Hex String encoded for a value of: e89b48f1e2016b78b805c1430d5a139e62bba9237ff3e34bad56dae6499b2647

JWT Use Cases
-------------

Let's take a moment to take stock of what this gives us.
We have a set of claims that can include any data at all including an issuer and an expiry time. Whenever your Security Token Service issues a token, it will be signed against claims. The claims on any token we recieve back can always be validated as no-one will have the secret key so any attempt to change the claims will result in a failure to validate the token.

A couple quick use cases:
If used between services behind a firewall, both the message creator and the receiver would have the secret key.
In this way, they can validate the authenticity of messages between servers.

For a user of a web application, a token can issued by the security token service on login. That can then be used for both writing use info in the UI from the claims and also for authenticating requests for other assets.
As an example, the claims could have username, age, timezone, etc which can be used on the client side.
Then the user always provides the token in an "authorization" header on requests for restricted resources such as creating a post on their timeline.
The token can be authenticated to validate the user is infact who they say they are. If used like this, the token can be used instead of, or in conjunction with, a session store.

The nice thing about JWT is that you use a header for the data so it gets around CORS problems seen with cookies. The token can be validated by any service that can validate the token with the secret key.

Usage Examples
==============

Creating a Token
----------------

You can follow along by running 'sbt console' and typing the commands.

We will recreate the token from the example here.
First, import jwt:

    import authentikat.jwt._

There are two classes you first need to create: JwtHeader and JwtClaimsSest

The header takes an Algorithm. Eg:

    val header = JwtHeader("HS256")

There are 3 ClaimsSet constructors. Here we'll use the Map[String, Any] constructor. Other options are passing in a Json4s JValue or a Json String.
You should check the spec to see what common public claims are in use.

We will use a private claim called 'Hey'. Eg:

    val claimsSet = JwtClaimsSet(Map("Hey" -> "foo"))

Once you have a JwtClaimsSet and JwtHeader, you can make a new token with your secret key:

    val jwt: String = JsonWebToken(header, claimsSet, "secretkey")

That will return your token - you can use it now!

    jwt: String = eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJIZXkiOiJmb28ifQ==.e89b48f1e2016b78b805c1430d5a139e62bba9237ff3e34bad56dae6499b2647

Validating a Token
------------------

Validating a token is easy - you just pass it in to a method with the key:
Note you validate the entire token - don't make the mistake of passing in the encrypted claims only.

    val isValid = JsonWebToken.validate(jwt, "secretkey")

isValid: Boolean = true

Parsing a Token
---------------

Pasing a Token is also simple thanks to an extractor. You just pattern match against the jwt string.
You will get the a JwtHeader, a JwtClaimsSetJvalue (json4s AST) and the signature.
We can import some implicits to pimp out the object and give it some extra functionality.
Here is an example that will give you back the claims in a scala.util.Try[Map[String, String]].
(It will fail if the json tree is not flat.)

    val claims: Option[Map[String, String]] = jwt match {
            case JsonWebToken(header, claimsSet, signature) =>
              claimsSet.asSimpleMap.toOption
            case x =>
              None
    }

This is the simplest way to get your data. Now you can work with at like any Option[Map[String, String].

    scala> claims.getOrElse(Map.empty[String, String]).get("Hey")
    res1: Option[String] = Some(foo)

For complex json (eg nested objects), it is possible to work with the json4s JValue directly to allow for more complex use cases.

    import org.json4s.JsonDSL._
    import org.json4s.JValue

    val claims: Option[JValue] = jwt match {
            case JsonWebToken(header, claimsSet, signature) =>
              Option(claimsSet.jvalue)
            case x =>
              None
    }

That will leave you with an Option[JValue]
Now you can work with the jvalue as described in the json4s documentation here: https://github.com/json4s/json4s
It's similar to the native scala xml parsing.

    scala> scala> (claims.get \ "Hey").values == "foo"
           res13: Boolean = true

Contributing
============

Contributions very welcome. Fork the repo and make a pull request.

Licensing
=========

See attached LICENSE file. Apache2 licenced.

Contributors
============

Jason Goodwin
