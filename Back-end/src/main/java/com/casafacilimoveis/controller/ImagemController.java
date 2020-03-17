package com.casafacilimoveis.controller;

import com.casafacilimoveis.model.entities.Anuncio;
import com.casafacilimoveis.model.entities.Imagem;
import com.casafacilimoveis.repository.AnuncioRepository;
import com.casafacilimoveis.repository.ImagemRepository;
import com.casafacilimoveis.service.GoogleDriveService;
import com.casafacilimoveis.util.Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Api(description = "Controller de requisições de imagens")
@RestController
@CrossOrigin
@RequestMapping("/file")
public class ImagemController {

    @Autowired
    private GoogleDriveService driveService;

    @Autowired
    private AnuncioRepository anuncioRepository;

    @Autowired
    private ImagemRepository imagemRepository;

    /**
     * https://developers.google.com/drive/api/v2/reference/files/insert
     *
     * @param files arquivos para fazer upload
     * @param id    do anuncio para inserção no banco de dados
     */
    @ApiOperation("Envio das imagens do anúncio")
    @PostMapping(path = "/v1/enviar-imagens-anuncio/{id}")
    public ResponseEntity salvarImagensAnuncio(@RequestParam("files") List<MultipartFile> files, @PathVariable("id") Integer id) {

        Anuncio anuncio = anuncioRepository.findOneById(id);

        List<Imagem> imagensSave = null;

        if (anuncio != null) {
            List<File> fileList = new ArrayList<>();
            //Convert de Multipart file to File
            if (files != null && files.size() > 0) {
                for (MultipartFile imagemConvert : files) {
                    try {
                        File file = Util.convert(imagemConvert);
                        fileList.add(file);
                    } catch (IOException e) {
                        System.out.println("Erro na conversão da imagem");
                    }
                }
            }

            imagensSave = new ArrayList<>();
            for (File imagem : fileList) {
                if (imagem != null) {
                    try {
                        com.google.api.services.drive.model.File file = driveService.uploadFile(imagem);
                        //Deleta o arquivo temporário criado para não haver problemas de sobrecarregamento do servidor.
                        imagem.delete();
                        imagensSave.add(new Imagem(file.getId(), anuncio));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            for (Imagem imagem : imagensSave) {
                imagemRepository.save(imagem);
            }

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (imagensSave != null) {
            return ResponseEntity.ok(imagensSave);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * DELETA UMA LISTA DE IMAGENS
     */
    public boolean deletarImagensAnuncio(List<Imagem> imagens) {
        if (imagens != null && imagens.size() > 0) {
            for (Imagem imagem : imagens) {
                try {
                    boolean deleteFile = driveService.deleteFile(imagem.getId());
                    if (!deleteFile) {
                        System.out.println("Não foi possível remover a imagem (URL): " + imagem.getImagemUrl());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }
}
