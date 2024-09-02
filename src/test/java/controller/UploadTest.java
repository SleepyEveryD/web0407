package controller;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class UploadTest {
    @Test
    void testUpload() {
        // 定义源文件路径和目标文件路径
        Path sourcePath = Paths.get("src/main/resources/DSC00795.jpg");
        Path targetPath = Paths.get("src/main/resources/1.png");

        try {
            // 确保目标目录存在
            Files.createDirectories(targetPath.getParent());

            // 复制文件
            Files.copy(sourcePath, targetPath);

            // 验证文件是否存在


        } catch (IOException e) {
            e.printStackTrace();
            // 测试失败
        }
    }


}