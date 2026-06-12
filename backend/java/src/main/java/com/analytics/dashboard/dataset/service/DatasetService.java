package com.analytics.dashboard.dataset.service;

import com.analytics.dashboard.dataset.model.Dataset;
import com.analytics.dashboard.dataset.repository.DatasetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
public class DatasetService {

    @Autowired
    private DatasetRepository datasetRepository;

    // Placeholder for S3 service integration
    // @Autowired
    // private S3Service s3Service;

    @Autowired
    private RestTemplate restTemplate;

    private final String pythonServiceUrl = "http://python-backend:5000/process-data"; // Assuming 'python-backend' is the service name in docker-compose

    public Dataset uploadDataset(MultipartFile file, Long userId) throws IOException {
        // Placeholder for S3 upload logic
        // String s3Key = s3Service.uploadFile(file);
        String s3Key = "mock-s3-key/" + file.getOriginalFilename(); // Mock S3 key

        Dataset dataset = new Dataset();
        dataset.setName(file.getOriginalFilename());
        dataset.setS3Key(s3Key);
        dataset.setStatus("UPLOADED");
        dataset.setUserId(userId);
        Dataset savedDataset = datasetRepository.save(dataset);

        // Trigger Python data processing service
        triggerPythonDataProcessing(savedDataset.getId(), file);

        return savedDataset;
    }

    private void triggerPythonDataProcessing(Long datasetId, MultipartFile file) {
        // In a real application, you might send the S3 key and other metadata
        // to the Python service, and the Python service would fetch the file from S3.
        // For simplicity, we're passing the file directly here (which might not be ideal for large files).
        // A better approach would be to upload to S3 first, then send the S3 key to Python.

        // For now, let's simulate sending the file to the Python service
        // This part needs to be adapted based on how the Python service expects the data.
        // For example, if Python expects a file upload, you'd need to reconstruct it.
        // If it expects an S3 key, you'd send that.

        // For demonstration, we'll just log that processing is triggered.
        System.out.println("Triggering Python data processing for dataset ID: " + datasetId);

        // Example of making a POST request to the Python service
        // MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        // body.add("file", new ByteArrayResource(file.getBytes()) {
        //     @Override
        //     public String getFilename() {
        //         return file.getOriginalFilename();
        //     }
        // });
        //
        // HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        //
        // HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        //
        // try {
        //     ResponseEntity<String> response = restTemplate.postForEntity(pythonServiceUrl, requestEntity, String.class);
        //     System.out.println("Python service response: " + response.getBody());
        //     // Update dataset status based on Python service response
        // } catch (Exception e) {
        //     System.err.println("Error calling Python service: " + e.getMessage());
        //     // Update dataset status to FAILED
        // }
    }

    public List<Dataset> getDatasetsByUserId(Long userId) {
        return datasetRepository.findByUserId(userId);
    }

    public Dataset getDatasetById(Long id) {
        return datasetRepository.findById(id).orElse(null);
    }
}