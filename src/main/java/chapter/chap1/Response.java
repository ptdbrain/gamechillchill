package chapter.chap1;

public class Response {
    private String text; // Nội dung phản hồi
    private String next; // ID hội thoại tiếp theo

    public Response(String text, String next) {
        this.text = text;
        this.next = next;
    }

    public String getText() {
        return text;
    }

    public String getNext() {
        return next;
    }
}
