# Kayla

Kayla is a JavaFX digital assistant powered by Amazon Lex. We'll use Java modules to give her functionality of different tasks. We'll then deploy these to AWS Lambda which has a connection to our bot and we can then call and execute these functions through Kayla.

## Usage
You need an AWS account. Set up a bot in Amazon Lex. Configure your bot as needed in the `KaylaEngine` class. Namely: 

```java
public class KaylaEngine{
  private static final AmazonLexRuntime client = AmazonLexRuntimeClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();
  private static String contentType() {
        return "audio/x-l16; sample-rate=16000";
    }

    private static String botName() {
        return "Kayla";
    }

    private static String botAlias() {
        return "kayla";
    }

    private static String userID() {
        return "fromIDE";
    }
}

```
For each command or task we issue to Kayla, you need to make an [Intent](https://docs.aws.amazon.com/lex/latest/dg/gs2-create-bot-intent.html) in Amazon Lex.  We can then use Amazon Lambda for complex requests from our bot. The project has modules in the `aws/lambda` directory where we can write different functions to deploy to Amazon Lambda.
## Contributing
She's opensource so if you have time to contribute, feel free to. The more modules and functionality we can add on to her, the better. For uniformity, modules are kept in the `aws/lambda` directory and let's make it good practice to leave instructions on configuring them in their own respective `README` files. 

## Project Status
Ongoing. I'll work on this as often as I can. Progress can be a little slow since I'm also juggling work on the side.