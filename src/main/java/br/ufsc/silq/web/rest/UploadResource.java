package br.ufsc.silq.web.rest;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class UploadResource {

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
		byte[] bytes;

		if (!file.isEmpty()) {
			bytes = file.getBytes();
		}

		log.debug("Received file {}", file.getOriginalFilename());

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
