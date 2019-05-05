package top.chorg.kernel.communication.api.announcements;

public class AddTemplateRequest {
    public String name, title, content;

    public AddTemplateRequest(String name, String title, String content) {
        this.name = name;
        this.title = title;
        this.content = content;
    }
}
