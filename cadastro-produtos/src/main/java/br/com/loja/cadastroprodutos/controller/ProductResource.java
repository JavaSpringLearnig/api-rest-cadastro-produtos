package br.com.loja.cadastroprodutos.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.cadastroprodutos.model.Products;
import br.com.loja.cadastroprodutos.service.ProductsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javassist.NotFoundException;

@RestController
@Api(description = "Gerenciamento de produtos")
@RequestMapping(value = "/products", produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
public class ProductResource {
		
	@Autowired
	ProductsService service;

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	@ApiOperation(value = "Efetua o cadastro de um produto")
	@ApiResponses(value = {@ApiResponse(code = 201, message = "Produto Criado com Sucesso!")})
	@ApiImplicitParams({@ApiImplicitParam(name = "product", value = "Estrutura para criação de produto.", dataType = "Cadastrar Produto", required = true)})
	
	public ResponseEntity<Products> createProduct(@Valid @RequestBody Products product) {
		Products products = service.createProduct(product);
		return new ResponseEntity<>(products, HttpStatus.CREATED);
	}

	@GetMapping
	@ApiOperation(value = "Lista os porodutos cadastrados")

	public ResponseEntity<?> listAllProducts(Pageable page) {
		return ResponseEntity.ok(service.listAllProducts(page));
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Retorna o poroduto com o 'id' informado")
	@ApiResponses(value = {@ApiResponse(code = 404, message = "Produto Não Encontrado!")})
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "Identificador do produto", dataType = "Long", required = true)})

	public ResponseEntity<Products> searchProductById(@PathVariable Long id) throws NotFoundException {
		Products products = service.searchProductById(id);
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@ApiResponses(value = {@ApiResponse(code = 404, message = "Produto Não Encontrado!")})
	@ApiOperation(value = "Altera o estoque do produto com o 'id' informado", notes = "Aumenta ou diminui a quantidade de estoque de um produto.")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "Identificador do produto", dataType = "Long", required = true),
		@ApiImplicitParam(name = "quantity", value = "Quantidade disponível em estoque", dataType = "int", required = true)
	})

	public ResponseEntity<Products> updateProduct(@PathVariable Long id, @RequestParam Integer quantity) throws NotFoundException {
		Products products = service.updateProduct(id, quantity);
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Deleta o produto com o 'id' informado", notes = "O produto deixara de existir permanentemente.")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "Identificador do produto", dataType = "Long", required = true)})
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Produto Excluído com Sucesso!"),
			@ApiResponse(code = 404, message = "Produto Não Encontrado!")})
	
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws NotFoundException  {
		service.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}
}
