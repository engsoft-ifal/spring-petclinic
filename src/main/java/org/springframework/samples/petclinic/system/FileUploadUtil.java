package org.springframework.samples.petclinic.system;

import java.io.*;
import java.nio.file.*;
// import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) {
		Path uploadPath = Paths.get(uploadDir);

		try {
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
		}
		catch (Exception e) {
			System.out.println("Could not save image file: " + fileName);
		}

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		}
		catch (Exception e) {
			System.out.println("Could not save image file: " + fileName);
		}
	}

}
