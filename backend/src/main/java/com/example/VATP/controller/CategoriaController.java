package com.example.VATP.controller;

import com.example.VATP.model.Categoria;
import com.example.VATP.model.Producto;
import com.example.VATP.service.CategoriaService;
import com.example.VATP.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    @Autowired
    private final CategoriaService categoriaService;
    @Autowired
    private final ProductoService productoService;

    public CategoriaController(CategoriaService categoriaService, ProductoService productoService) {
        this.categoriaService = categoriaService;
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<Categoria> guardarCategoria(@RequestBody Categoria categoria) {
        Categoria savedCategoria = categoriaService.guardarCategoria(categoria);
        return ResponseEntity.ok(savedCategoria);
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerTodas() {
        List<Categoria> categorias = categoriaService.obtenerTodas();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerCatPorId(@PathVariable Integer id) {
        Optional<Categoria> categoria = categoriaService.obtenerCatPorId(id);
        return categoria.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Integer id, @RequestBody Categoria categoria) {
        Optional<Categoria> existingCategoria = categoriaService.obtenerCatPorId(id);
        if (existingCategoria.isPresent()) {
            categoria.setId(id);
            Categoria updatedCategoria = categoriaService.actualizarCategoria(categoria);
            return ResponseEntity.ok(updatedCategoria);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

/*

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Integer id) {

        List<Producto> productosConCategoria = productoService.obtenerTodos();

        for (Producto producto : productosConCategoria) {
            Categoria categoriaProducto = producto.getCategoria();
            if (categoriaProducto != null && Objects.equals(categoriaProducto.getId(), id)) {
                productoService.eliminarProducto(producto.getId());
            }
        }

        categoriaService.eliminarCategoria(id);

        return ResponseEntity.noContent().build();
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Integer id) {
        List<Producto> productosConCategoria = productoService.obtenerProductosPorCategoria(id);

        for (Producto producto : productosConCategoria) {
            productoService.eliminarProducto(producto.getId());
        }

        categoriaService.eliminarCategoria(id);

        return ResponseEntity.noContent().build();
    }

}