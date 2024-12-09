package chapter.chap1;

import java.util.List;

public class Dialogue {
    private String id; // ID của hội thoại
    private String text; // Nội dung hội thoại
    private List<Response> responses; // Danh sách các tùy chọn phản hồi

    public Dialogue(String id, String text, List<Response> responses) {
        this.id = id;
        this.text = text;
        this.responses = responses;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public List<Response> getResponses() {
        return responses;
    }
}
