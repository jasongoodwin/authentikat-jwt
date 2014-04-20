authentikat-jwt
===============

A simple scala-jwt library.


JWT - A Claims Based Authentication
===================================

Before we get into how to use the library, let's assume we have a secret key, and the key is "secret-key".
Let's start the usage example imagining we reveive an http request with a header containing a JWT (pronounced "Jot")
We need to validate this token is okay. First, let's look at the structure of a JWT.

A token consists of 3 period separated values. A token might look like this:

    eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJIZXkiOiJmb28ifQ==.e89b48f1e2016b78b805c1430d5a139e62bba9237ff3e34bad56dae6499b2647
    ^JwtHeader                           ^JwtClaimsSet        ^JsonWebSignature - One way hash of the JwtClaimsSet

The JwtHeader and JwtClaimsSet are Base64Encoded Json - they can be decoded and used.
In the example above, they contain the following simple json:

    Header:
    {"alg":"HS256","typ":"JWT"}

    ClaimsSet:
    {"Hey":"foo"}

JwtHeader
---------

The header contains the algorith used for the Signature - in this case HMAC using SHA-256 .
And also contains an optional cty field - "JWT" to indicate this is a Json Web Token.
While not supported by AuthentikatJwt, the It can also contain a content type ("cty") field
This field would indicate if the content if another token.
This feature is not currently supported but you could include another token in the claims.

(Note: At time of writing supported algorithms are:
- "none"
- "HS256"
- "HS384"
- "HS512"

JwtClaimsSet
------------

The Claims set can contain any number of public or private claims.
The public claims are defined in the JWT draft here: http://self-issued.info/docs/draft-ietf-oauth-json-web-token.html
They include items like Issue (iss) that indicates who the token was created by. It can contain an Expiry (exp) as well to indicate
when the token is expired.

Signature
---------

Finally, there is a one way hash of the Base64 encoded claims set.
In the example, it's a hash using a secret key "secretkey" of the string "eyJIZXkiOiJmb28ifQ==" using Hmac SHA256.
The result is Hex String encoded for a value of: e89b48f1e2016b78b805c1430d5a139e62bba9237ff3e34bad56dae6499b2647

JWT Use Cases
-------------

Let's take a quick minute to take stock of what this gives us.
We have a set of claims that can include any data at all including an issuer and an expiry. If someone gets ahold of a token,
it can then be validated as original against the secret key. The claims can basically be validated as no-one will have the secret key.

A couple quick use cases:
If used between services behind a firewall, both the message creator and the receiver would have the secret key.
In this way, they can validate the authenticity of messages between servers.

For a user of a web application, a token can be sent back on login which can be used in the client to render user info.
So for example the claims could have username, age, timezone, etc which can be used on the client side.
Then the user always provides the token on request for restricted resources in an API (creating an article via REST for example.)
The token can be authenticated to validate the user is infact who they say they are. If used like this, the token can be used instead of cookie store.
This gets around CORS problems as the token can be sent to any service.
Because of this, JWT is a good authentication strategy for OAUTH2 implementations.

Usage Examples
==============

Adding the Dependency
---------------------

Note on Dependency management in SBT - I'm waiting on a Sonatype repository to make public.

For now, run

    sbt publish-local

Then add dependency to your project:

    "com.jason-goodwin" %% "authentikat-jwt" % "0.1.0"

Creating a Token
----------------

You can follow along by running 'sbt console' and typing the commands.

We will recreate the token from the example here.
First, import jwt:

    import authentikat.jwt._

There are two classes you first need to create: JwtHeader and JwtClaimsSest

The header takes an Algorithm. Eg:

    val header = JwtHeader("HS256")

The claims take a Map[String, Any]. You should check the spec to see what common public claims. Eg:

    val claimsSet = JwtClaimsSet(Map("Hey" -> "foo"))

Once you have a JwtClaimsSet and JwtHeader, you can make a new token with your secret key:

    val jwt: String = JsonWebToken(header, claimsSet, "secretkey")

There is your token - you can use it now!

Validating a Token
------------------

Validating a token is easy - you just pass it in to a method with the key:
Note you validate the entire token - don't make the mistake of passing in the encrypted claims only.

    val isValid = JsonWebToken.validate(jwt, "secretkey")

isValid: Boolean = true

Parsing a Token
---------------

Pasing a Token is also simple thanks to an extractor. You just pattern match against the jwt string.
Here is an example that will give you back an Option with both the header and the claims set.

    val headerAndClaims: Option[(JwtHeader, Map[String, String])] = jwt match {
            case JsonWebToken(header, claimsSet, signature) =>
              Some((header, claimsSet.claims))
            case x =>
              None
    }

Getting at the Claims
---------------------

Now that we have everything extracted, we can access the claim like any other map:

    claimsSet.get("Hey") == Some("foo")