package rtkay.lex;

public class Message {
    private String contentType;
    private String content;

    public String getContentType() {
        return contentType;
    }

    public String getContent() {
        return content;
    }

    public Message(String contentType) {
        this.contentType = contentType;
    }
    public Message(String contentType, String content) {
        this.content = content;
        this.contentType = contentType;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
