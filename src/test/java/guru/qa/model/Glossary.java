package guru.qa.model;

import com.google.gson.annotations.SerializedName;

public class Glossary {
    public String title;
    @SerializedName("GlossDiv")
    public GlossDiv glossDiv;

    public static class GlossDiv {
        public String title;
        public Boolean flag;
    }
}
