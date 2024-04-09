package it.challenging.torchy.util.ai;

import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.model.ModelClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@FunctionalInterface
public interface ImageClient extends ModelClient<ImagePrompt, ImageResponse> {

    ImageResponse call(ImagePrompt request);

}