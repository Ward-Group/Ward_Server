package com.ward.ward_server.webcrawling;

import com.ward.ward_server.webcrawling.entity.Item;
import com.ward.ward_server.webcrawling.webCrawler.KasinaWebCrawler;
import com.ward.ward_server.webcrawling.webCrawler.NikeWebCrawler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebCrawlingService {
    private final ItemRepository itemRepository;
    private final NikeWebCrawler nike;
    private final KasinaWebCrawler kasina;

    public void getNikeData() {
        nike.getData().stream()
                .map(e -> Item.builder()
                        .name(e.name())
                        .image(e.imgUrl())
                        .siteUrl(e.siteUrl())
                        .releaseDate(e.releaseDate())
                        .dueDate(e.dueDate())
                        .presentationDate(e.presentationDate())
                        .state(e.state())
                        .brand(e.brand())
                        .build())
                .forEach(e -> itemRepository.save(e));
    }

    public void getKasinaData() {
        kasina.getData().stream()
                .map(e -> Item.builder()
                        .name(e.name())
                        .image(e.imgUrl())
                        .siteUrl(e.siteUrl())
                        .releaseDate(e.releaseDate())
                        .dueDate(e.dueDate())
                        .presentationDate(e.presentationDate())
                        .state(e.state())
                        .brand(e.brand())
                        .build())
                .forEach(e -> itemRepository.save(e));
    }

}
