package view.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;

public class XmlWrapper<T> {
	private List<T> items;
	 
    public XmlWrapper() {
        items = new ArrayList<T>();
    }
 
    public XmlWrapper(List<T> items) {
        this.items = items;
    }
 
    @XmlAnyElement(lax=true)
    public List<T> getItems() {
        return items;
    }
}
