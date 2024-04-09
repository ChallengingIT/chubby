package it.challenging.torchy.util.ai;

import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImageResponseMetadata;
import org.springframework.ai.model.ModelResponse;

import java.util.List;

public class ImageResponse implements ModelResponse<ImageGeneration> {

    private final ImageResponseMetadata imageResponseMetadata;

    private final List<ImageGeneration> imageGenerations;

    public ImageResponse(ImageResponseMetadata imageResponseMetadata, List<ImageGeneration> imageGenerations) {

        this.imageResponseMetadata = imageResponseMetadata;
        this.imageGenerations      = imageGenerations;
    }

    @Override
    public ImageGeneration getResult() {
        return imageGenerations.stream().findFirst().isPresent() ? imageGenerations.stream().findFirst().get() : null;
    }

    @Override
    public List<ImageGeneration> getResults() {
        return this.imageGenerations;
    }

    @Override
    public ImageResponseMetadata getMetadata() {
        return this.imageResponseMetadata;
    }
}