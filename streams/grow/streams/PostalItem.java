package grow.streams;

public class PostalItem {

    private float weight;
    private String address;
    private String from;
    private String to;
    private int priority;

    public PostalItem(float weight, String address, String from, String to, int priority) {
        this.weight = weight;
        this.address = address;
        this.from = from;
        this.to = to;
        this.priority = priority;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PostalItem)) {
            return false;
        }
        PostalItem that = (PostalItem) obj;
        if (weight == that.weight && address.equals(that.getAddress()) && from.equals(that.from)
             && to.equals(that.to) && priority == that.priority) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Address: [" + address + "]; weight [" + weight + "]; priority - " + priority;
    }
}
