package utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class UtilTest {

    @Test
    void writeTableToHtml() {
        String html = Util.writeTableToHtml("createGroup", new String[][]{{"a", "b", "c"}, {"d", "e", "f"}},null,null);
        System.out.println(html);
    }
    @Test
    void readHtml() throws IOException {
        String html = Util.readHTMLFile("src/main/webapp/createGroup.html");
        System.out.println(html);

    }
    @Test
    void testss(){
        System.out.println(Util.selectMembersStatic);
    }
}