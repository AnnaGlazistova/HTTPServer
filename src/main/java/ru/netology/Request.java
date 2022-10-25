package ru.netology;

import java.util.ArrayList;
import java.util.HashMap;

public class Request {
    private final String method;
    private final String path;
    private ArrayList<HashMap<String, String>> query;

    public Request(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public void setQuery(String queryString) {
        var targetQuery = new ArrayList<HashMap<String, String>>();
        var listOfParams = queryString.split("&");
        for (int i = 0; i < listOfParams.length; i++) {
            var pair = listOfParams[i].split("=");
            targetQuery.add(new HashMap<String, String>() {{
                put(pair[0], pair[1]);
            }});
        }
        this.query = targetQuery;
    }

    public ArrayList<HashMap<String, String>> getQueryParam(String name) {
        var targetQuery = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < this.query.size(); i++) {
            if (this.query.get(i).containsKey(name)) {
                targetQuery.add(this.query.get(i));
            }
        }
        return targetQuery;
    }

    public ArrayList<HashMap<String, String>> getQueryParams() {
        return query;
    }
}