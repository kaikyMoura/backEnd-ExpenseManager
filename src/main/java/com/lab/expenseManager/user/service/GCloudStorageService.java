package com.lab.expenseManager.user.service;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Base64;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.api.core.ApiFuture;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.BlobWriteSession;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobWriteOption;
import com.google.cloud.storage.StorageOptions;
import com.google.common.io.ByteStreams;

@Service
public class GCloudStorageService {
	
	private final RestTemplate restTemplate = new RestTemplate();

	public String uploadFile(String file) throws FileNotFoundException, IOException {
		if (file == null || file.isEmpty()) {
			return null;
		}

		String[] parts = file.split(",");
		String imageData = parts.length > 1 ? parts[1] : parts[0];

		// Decodifica a string Base64 para bytes
		byte[] imageBytes = Base64.getDecoder().decode(imageData);

		Storage storage = StorageOptions.newBuilder().build().getService();

		String bucketName = "expensemanager-assets-storage";
		String blobName = UUID.randomUUID() + "_image.jpg";

		ReadableByteChannel readableByteChannel = Channels.newChannel(new ByteArrayInputStream(imageBytes));
		BlobId blobId = BlobId.of(bucketName, blobName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();

		try {
			BlobWriteSession blobWriteSession = storage.blobWriteSession(blobInfo, BlobWriteOption.doesNotExist());
			try (WritableByteChannel writableByteChannel = blobWriteSession.open()) {

				ByteStreams.copy(readableByteChannel, writableByteChannel);
			}
			ApiFuture<BlobInfo> resultFuture = blobWriteSession.getResult();
			BlobInfo uploadedBlobInfo = resultFuture.get();

			// Retorna a URL pública do arquivo para ser salva no banco de dados
			return String.format("https://storage.googleapis.com/%s/%s", bucketName, uploadedBlobInfo.getName());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao fazer upload da imagem", e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao obter os resultados do upload", e);
		}
	}

	public byte[] getFile(String path) {
		try {
			ResponseEntity<byte[]> response = restTemplate.getForEntity(path, byte[].class);

			if (response.getStatusCode().is2xxSuccessful()) {
				return response.getBody();
			} else {
				throw new RuntimeException(
						"Erro ao buscar imagem: Código de resposta " + response.getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao buscar imagem", e);
		}
	}
}