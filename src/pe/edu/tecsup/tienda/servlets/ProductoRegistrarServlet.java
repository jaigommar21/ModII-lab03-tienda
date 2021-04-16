package pe.edu.tecsup.tienda.servlets;

import java.io.File;
import java.io.IOException;

import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;
import pe.edu.tecsup.tienda.entities.Categoria;
import pe.edu.tecsup.tienda.entities.Producto;
import pe.edu.tecsup.tienda.services.CategoriaService;
import pe.edu.tecsup.tienda.services.ProductoService;

@WebServlet("/ProductoRegistrarServlet")

@MultipartConfig(fileSizeThreshold = 1024 * 1024, 
				 maxFileSize = 1024 * 1024 * 5, 
				 maxRequestSize = 1024 * 1024 * 5 * 5)
public class ProductoRegistrarServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ProductoListarServlet.class);
	private CategoriaService categoriaService;
	private ProductoService productoService;

	public ProductoRegistrarServlet() {
		this.productoService = new ProductoService();
		this.categoriaService = new CategoriaService();
	}

	/**
	 *  Invocacion realizada directamente por un HREF
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("Get ProductoRegistrarServlet");
		try {
			List<Categoria> categorias = categoriaService.listar();
			request.setAttribute("categorias", categorias);
			request.getRequestDispatcher("/WEB-INF/jsp/producto/registrar.jsp").forward(request, response);
		} catch (Exception e) {
			log.error(e, e);
			throw new ServletException(e.getMessage(), e);
		}
	}

	/**
	 * Invocacion realizada por un FORM con action = POST
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		log.info("Post ProductoRegistrarServlet");
		
		try {
		
			// Lee datos del request
			String categorias_id = request.getParameter("categorias_id");
			String nombre = request.getParameter("nombre");
			String precio = request.getParameter("precio");
			String stock = request.getParameter("stock");
			String descripcion = request.getParameter("descripcion");
			
			
			
			
			
			// Tratamiento de los datos
			Producto producto = new Producto();
			producto.setCategorias_id(Integer.parseInt(categorias_id));
			producto.setNombre(nombre);
			producto.setPrecio(Double.parseDouble(precio));
			producto.setStock(Integer.parseInt(stock));
			producto.setDescripcion(descripcion);
			
			
			Part part = request.getPart("imagen");
			
			if(part.getSubmittedFileName() != null) { 
				File filepath = new File(getServletContext().getRealPath("") 
							+ File.separator + "files"); 
				if (!filepath.exists()) 
					filepath.mkdir(); 
				
				String filename = System.currentTimeMillis() + "-" + part.getSubmittedFileName(); 
				part.write(filepath + File.separator + filename); 
				
				log.info("Imagen cargada en: " + filepath + File.separator + filename); 
				
				producto.setImagen_nombre(filename); 
				producto.setImagen_tipo(part.getContentType()); 
				producto.setImagen_tamanio(part.getSize()); 
			}
			
			
			
			
			
			log.info(producto);
			
			// Graba en la BBDD
			productoService.registrar(producto);
			
			// Redirecciona salida 
			response.sendRedirect(request.getContextPath() + "/ProductoListarServlet");
			
		} catch (Exception e) {
			log.error(e, e);
			throw new ServletException(e.getMessage(), e);
		}
	}
}