package Utils;

public abstract class Event<DATA,TIP> {
    private DATA data;
    private TIP tip;


    public Event(DATA data, TIP tip) {
        this.data = data;
        this.tip = tip;
    }


    public DATA getData() {
        return data;
    }

    public void setData(DATA data) {
        this.data = data;
    }

    public TIP getTip() {
        return tip;
    }

    public void setTip(TIP tip) {
        this.tip = tip;
    }
}
