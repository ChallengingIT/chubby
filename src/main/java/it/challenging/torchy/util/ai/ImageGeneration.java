package it.challenging.torchy.util.ai;

import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageGenerationMetadata;
import org.springframework.ai.model.ModelResult;

public class ImageGeneration implements ModelResult<Image> {

    private ImageGenerationMetadata imageGenerationMetadata;

    private Image image;

    @Override
    public Image getOutput() {
        return this.image;
    }

    @Override
    public ImageGenerationMetadata getMetadata() {
        return this.imageGenerationMetadata;
    }

}