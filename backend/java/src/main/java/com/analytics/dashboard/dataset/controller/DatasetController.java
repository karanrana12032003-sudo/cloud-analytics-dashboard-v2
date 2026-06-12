package com.analytics.dashboard.dataset.controller;

import com.analytics.dashboard.dataset.model.Dataset;
import com.analytics.dashboard.dataset.service.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/datasets")
public class DatasetController {

    @Autowired
    private DatasetService datasetService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDataset(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) {
        try {
            Dataset dataset = datasetService.uploadDataset(file, userId);
            return ResponseEntity.ok(dataset);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Could not upload the file: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Dataset>> getDatasetsByUserId(@PathVariable Long userId) {
        List<Dataset> datasets = datasetService.getDatasetsByUserId(userId);
        return ResponseEntity.ok(datasets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dataset> getDatasetById(@PathVariable Long id) {
        Dataset dataset = datasetService.getDatasetById(id);
        if (dataset != null) {
            return ResponseEntity.ok(dataset);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}