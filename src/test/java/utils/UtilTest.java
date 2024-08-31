package utils;

import beans.Album;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

class UtilTest {
    @Test
    void generateHomePageHtmlTest() {
        List<Album> myAlbums = Arrays.asList(
                new Album(1, "Title1", new Date(), "user1"),
                new Album(2, "Title2", new Date(), "user2")
        );

        List<Album> otherAlbums = Arrays.asList(
                new Album(3, "Title3", new Date(), "user3"),
                new Album(4, "Title4", new Date(), "user4")
        );
        String generatedHtml = Util.generateHomePageHtml(myAlbums, otherAlbums);
        System.out.println(generatedHtml);



    }
/*
    @Test
    void writeTableToHtml() {
        String html = Util.writeTableToHtml("createGroup", new String[][]{{"a", "b", "c"}, {"d", "e", "f"}},null,null,null);
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

 */
}