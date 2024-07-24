package privategpt.dto;

import java.util.List;

public class CompletionResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;

    // Getter und Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public static class Choice {
        private String finish_reason;
        private Object delta; // Falls delta immer null ist, kann das auch ein anderer Typ sein
        private Message message;
        private Object sources; // Falls sources immer null ist, kann das auch ein anderer Typ sein
        private int index;

        // Getter und Setter
        public String getFinish_reason() {
            return finish_reason;
        }

        public void setFinish_reason(String finish_reason) {
            this.finish_reason = finish_reason;
        }

        public Object getDelta() {
            return delta;
        }

        public void setDelta(Object delta) {
            this.delta = delta;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

        public Object getSources() {
            return sources;
        }

        public void setSources(Object sources) {
            this.sources = sources;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    public static class Message {
        private String role;
        private String content;

        // Getter und Setter
        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
