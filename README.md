# LOOKING FOR MAINTAINER
Sorry guys - I'm having a hard time maintaining this library right now. There are several other JWT libraries that are much more mature than this as they have been maintained and used quite rigorously. This is a security library and was my own weekend hack project to start. I never ran it in production - I was just mostly curious about JWT as a standard in its early days. But it picked up a fair number of users.
I'd recommend you use the best supported and most widely used JWT implementation you can find! JWT is widely criticized in the security community so if you're going to use it, you should ensure that the implementation is very well used, and has had lots of eyes on it, and given the explosion of knowledge and interest, and especially the number of imperfect implementations, I feel that it is in your best interest to try to use the most mature code you can for these concerns if you're going to implement security features with JWT. 

authentikat-jwt - Claims Based JWT Implementation for Scala
===========================================================

![Build Status](https://travis-ci.org/jasongoodwin/authentikat-jwt.svg?branch=master)
![Current Version](https://img.shields.io/badge/version-0.4.5-brightgreen.svg?style=flat "0.4.5")
[![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202-blue.svg)](LICENSE)
[![Join the chat at https://gitter.im/jasongoodwin/authentikat-jwt](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/jasongoodwin/authentikat-jwt?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-brightgreen.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org) <a href="https://typelevel.org/cats/"><img src="https://typelevel.org/cats/img/cats-badge.svg" height="40px" align="right" alt="Cats friendly" /></a>

A simple scala-jwt library.
JWT - pronounced 'jot' - is a claims-based authentication standard. 

1.0.0 Milestone Status
======================

Currently 1.0.0-M1 is in central which has experimental changes:
- All signing algorithms supported (RSA, PS, ES, HS)
- Separate verifier and signer to allow eg public and private keys depending on signing algorithm
- Because the verifier must be explicitly passed, the API is inherently safer in ensuring the token is signed with the expected algorithm.

When released, the API will contain additional methods to aid in validating different areas of the tokens.


Setup
=====

Authentikat-Jwt has been published for scala 2.11, 2.12, 2.13:

Add the resolver and the dependency:

    resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

    libraryDependencies ++= Seq(
      "com.jason-goodwin" %% "authentikat-jwt" % "0.5.0"
    )

JWT - A Claims Based Authentication
===================================

Before we get into how to use the library, we'll explore a usage example to demonstrate how claims based authentication can be used.

Let's assume we have a secret key, and the key is "secretkey".
Let's start the usage example imagining we have an http based service or web application. We receive an http request to do something in our http server application with a header containing a JWT (pronounced "Jot")
We need to validate the request is authenticated by analyzing the JWT. First, let's look at the structure of a JWT.

A token consists of 3 dot separated values. The token our server receives:

    eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJIZXkiOiJmb28ifQ.fTW9f2w5okSpa7u64d6laQQbpBdgoTFvIPcx5gi70R8
    ^JwtHeader                           ^JwtClaimsSet      ^JsonWebSignature - One way hash of the JwtHeader and JwtClaimsSet

The JwtHeader and JwtClaimsSet are Base64Encoded Json - they can be decoded and used. The signature is used to validate the claims were created by someone with the secret key and to ensure they have not been modified since issuing.

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

- alg: It contains the algorithm used for the signature - in the example case, HMAC SHA-256.
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
    {"Hey":"foo"} + Base64Encoding = eyJIZXkiOiJmb28ifQ

The second part of the token is the claims.
Jwt is claims based authentication. See: http://en.wikipedia.org/wiki/Claims-based_identity

The Claims Set can contain any number of public or private claims. Because the claims are used to create a signature with a secret key, they cannot be modified after the token is signed.

The optional public claims that the standard supports are defined in the JWT draft here: http://self-issued.info/docs/draft-ietf-oauth-json-web-token.html
They include items like Issuer (iss) that indicates who the token was created by. It can contain an Expiry (exp) to indicate when the token will expire. 

Private claims can be anything (but obviously should not duplicate keys in the public claims space). Consider data that might be used to draw user session info in a UI. Including a username in the private claims is useful for validating the token is being used by the appropriate user.

Signature
---------

    {"Hey":"foo"} + Base64Encoding + HmacSha256 = fTW9f2w5okSpa7u64d6laQQbpBdgoTFvIPcx5gi70R8

The third portion of the token is a one way hash of the Base64 encoded claims set.
In the example, it's a hash using a secret key "secretkey" of the string "eyJIZXkiOiJmb28ifQ" using HMAC SHA256.
The result is a value of: fTW9f2w5okSpa7u64d6laQQbpBdgoTFvIPcx5gi70R8

JWT Use Cases
-------------

Let's take a moment to take stock of what this gives us.
We have a set of claims that can include any data at all including an issuer and an expiry time. Whenever your Security Token Service issues a token, it will be signed against claims. The claims on any token we receive back can always be validated as no-one will have the secret key so any attempt to change the claims will result in a failure to validate the token.

A couple quick use cases:
If used between services behind a firewall, both the message creator and the receiver would have the secret key.
In this way, they can validate the authenticity of messages between servers.

For a user of a web application, a token can issued by the security token service on login. That can then be used for both writing use info in the UI from the claims and also for authenticating requests for other assets.
As an example, the claims could have username, age, timezone, etc which can be used on the client side.
Then the user always provides the token in an "authorization" header on requests for restricted resources such as creating a post on their timeline.
The token can be authenticated to validate the user is in fact who they say they are. If used like this, the token can be used instead of, or in conjunction with, a session store.

The nice thing about JWT is that you use a header for the data so it gets around CORS problems seen with cookies. The token can be validated by any service that can validate the token with the secret key.

Usage Examples
==============

Creating a Token
----------------

You can follow along by running 'sbt console' and typing the commands.

We will recreate the token from the example here.
First, import jwt:

    import authentikat.jwt._

There are two classes you first need to create: JwtHeader and JwtClaimsSet

The header takes an Algorithm. Eg:

    val header = JwtHeader("HS256")

There are 3 ClaimsSet constructors. Here we'll use the Map[String, Any] constructor. Other options are passing in a Json4s JValue or a Json String.
You should check the spec to see what common public claims are in use.

We will use a private claim called 'Hey'. Eg:

    val claimsSet = JwtClaimsSet(Map("Hey" -> "foo"))

Once you have a JwtClaimsSet and JwtHeader, you can make a new token with your secret key:

    val jwt: String = JsonWebToken(header, claimsSet, "secretkey")

That will return your token - you can use it now!

    jwt: String = eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJIZXkiOiJmb28ifQ.fTW9f2w5okSpa7u64d6laQQbpBdgoTFvIPcx5gi70R8

Validating a Token
------------------

Validating a token is easy - you just pass it in to a method with the key:
Note you validate the entire token - don't make the mistake of passing in the encrypted claims only.

    val isValid = JsonWebToken.validate(jwt, "secretkey")

isValid: Boolean = true

Parsing a Token
---------------

Parsing a Token is also simple thanks to an extractor. You just pattern match against the jwt string.
You will get the a JwtHeader, a JwtClaimsSetJValue (json4s AST) and the signature.
We can import some implicits to pimp out the object and give it some extra functionality.
Here is an example that will give you back the claims in a scala.util.Try[Map[String, String]].
(It will fail if the json tree is not flat.)

    val claims: Option[Map[String, String]] = jwt match {
            case JsonWebToken(header, claimsSet, signature) =>
              claimsSet.asSimpleMap.toOption
            case x =>
              None
    }

This is the simplest way to get your data. Now you can work with it like any Option[Map[String, String]].

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
Now you can work with the JValue as described in the json4s documentation here: https://github.com/json4s/json4s
It's similar to the native scala xml parsing.

    scala> (claims.get \ "Hey").values == "foo"
           res13: Boolean = true

Contributing
============

Contributions very welcome. Fork the repo and make a pull request.
Please validate any JWTs created here after making a change: http://jwt.io/ 
Or create a test with a known correct JWT :)

Licensing
=========

See attached [LICENSE](LICENSE) file. Apache2 licenced.

Contributors
============

Jason Goodwin
