# dad-jokes

This module uses Retrofit to retrieve dad jokes from [icanhazdadjoke](icanhazdadjoke.com/). It also supports searching for a joke with a particular search word.

## About Artifact

You can find the [jar](https://github.com/kawaki-san/DigitalAssistant/tree/master/out/artifacts/dad_jokes_jar)  artifact to deploy to Amazon Lambda readily made in the project (Kayla) level `out` directory. Or you can build from source and deploy yours to Amazon Lambda. Skip the next section (Dependencies) if you are to use the provided artifact.

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
 rtkay.GetDadJoke::handleRequest
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
    "name": "TellAJoke",
    "slots": {
      "Subject": "cars"
    }
  }
}
```
Configure your bot information as required. Then test your bot.

### Amazon Lex side
####Create a slot type

Back in Amazon Lex. In `Slot types` create a slot type and name it `JokeSubject`. Under `Slot Resolution` , leave it on `Expand Results` and leave sample values. I had "cars", "houses", "apples" as sample values. 

####Create an intent
Create an Intent in your bot. Name it `TellAJoke`. For sample utterances, you can have:
```
Tell me a joke.
```
As well as 
```
Tell me a joke about {Subject}
```
In slots, create a slot with the following:
```
Required: Unchecked
Name: Subject
SlotType: JokeSubject
Prompt: About what? 
```
Check if the `{Subject}` part in your `Sample utterances` is the same colour as the one in your `Slots` section.
You can change your prompt to be whatever you want.

Under fulfillment, set it to `AWS Lambda Function` and pick the function you created to tell a joke. 

Save your intent and build the bot.

## Contributions
Pull requests are welcome. 
