package com.ward.ward_server.api.webcrawling;

import com.ward.ward_server.api.webcrawling.dto.WebProductData;
import com.ward.ward_server.api.webcrawling.entity.Item;
import com.ward.ward_server.api.webcrawling.webCrawler.KasinaWebCrawler;
import com.ward.ward_server.api.webcrawling.webCrawler.NikeWebCrawler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<WebProductData> test() {
        return nike.getData();
    }
}
