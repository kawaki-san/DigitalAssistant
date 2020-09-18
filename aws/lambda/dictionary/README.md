# dictionary

This module uses Retrofit to retrieve word definitions and sentence examples from the [OxfordAPI](https://developer.oxforddictionaries.com/). 

## Setting Up

You need to create an account over at Oxford [OxfordAPI](https://developer.oxforddictionaries.com/) and simply enter the `app_key` and `app_id` to the `OxfordAPI` class. 

## Dependencies

I had issues with later versions of Retrofit so I stuck with the ones made around the July 2016 period. It worked out because they were generally smaller in size compared to their more recent releases.

Use SDK 11 in the project. It's the latest supported JDK version in Lambda. 
The libraries can be found on Maven Central.

Library  | Version
------------- | -------------
Retrofit  | [2.1.0](https://mvnrepository.com/artifact/com.squareup.retrofit2/retrofit/2.1.0)
Okio  | [1.8.0](https://mvnrepository.com/artifact/com.squareup.okio/okio/1.8.0)
Retrofit Converter (Gson)  | [2.1.0](https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-gson/2.1.0)
Google Gson  | [2.8.6](https://search.maven.org/artifact/com.google.code.gson/gson/2.8.6/jar)
OkHttp  | [3.3.1](https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp/3.3.1)
AWS Lambda Java Core  | [1.2.1](https://mvnrepository.com/artifact/com.amazonaws/aws-lambda-java-core/1.2.1)
AWS Java SDK Lambda | [1.11.851](https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk-lambda/1.11.851)

For the last two, you can try with the latest versions. I only used the older Retrofit libraries because I had trouble getting it to work with the later verisons. Older versions were smaller in size so that worked out. 

## Deployment
### Amazon Lambda side
First we deploy the jar to Amazon Lambda. Create a function in Amazon Lambda. Give it a name and set the runtime to Java 11.
You can then scroll to the `Function Code` section and under `Actions`, click upload jar or zip. Select the artifact and upload it. Back in Amazon Lambda, scroll to `Basic settings` and click `Edit`. Under `Handler` set it to:
 ```
 com.rtkay.Oxford::handleRequest
``` 
Save your changes. Back in Amazon Lambda, click `Test` function. Configure your test event. You can use this:
```json
{
  "bot": {
    "alias": "kayla",
    "name": "Kayla",
    "version": "$LATEST"
  },
  "currentIntent": {
    "confirmationStatus": "None",
    "name": "definitions",
    "slots": {
      "word": "professor"
    }
  }
}
```
Configure your bot information as required. Then test your bot.

### Amazon Lex side

####Create an intent
#####1. Definition of a word
Create an Intent in your bot. Name it `definitions`. For sample utterances, you can have:
```
Define {word}
```
In slots, create a slot, for instance:
```
Required: checked
Name: word
SlotType: AMAZON.AlphaNumeric
Prompt: Which word? 
```

Under fulfillment, set it to `AWS Lambda Function` and pick the function you created for the dictionary.

#####2. Using a word in a sentence of a word
Create an Intent in your bot. Name it `use_in_sentence`. For sample utterances, you can have:
```
Use {word} in a sentence
```
Use the same slot information as above. If you're to change the intent names, make sure they are the same in the module and in Lex.

Save your intent and build the bot.

## Contributions
Pull requests are welcome. 
