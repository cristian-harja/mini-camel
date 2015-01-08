package mini_camel;


import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

/**
 * Created by MohammedYassine on 08/01/2015.
 */
public class SymTable
{
    Stack<TreeMap<String,Id>> st;

    public SymTable() {
        this.st = new Stack();
    }

    public void push()
    {
        st.push(new TreeMap<String, Id>());
    }

    public void pop()
    {
        st.pop();
    }

    public void put(String name, Id id)
    {
        TreeMap tmp = st.peek();
        tmp.put(name,id);
    }

    public TreeMap<String,Id> getStackElement(int index)
    {

        if (index == 0)
            return st.peek();


        TreeMap<String,Id> x = st.pop();
        try{
            return getStackElement(index-1);
        }
        finally{
            st.push(x);
        }
    }

    public Id get(String name)
    {
        TreeMap<String,Id> result;
        int i = 0;

        while(i <= st.size())
        {
            result = getStackElement(i);
            for(String key: result.keySet())
            {
                if(key.equals(name))
                {
                    return result.get(key);
                }
            }
            i ++;
        }
        return null;
    }

}
