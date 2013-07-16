/**
 * 
 */
package net.peaxy.simulator.web.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URI;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import net.peaxy.simulator.conf.RuntimeParameter;
import net.peaxy.simulator.web.handler.WebRequestHandler;

import org.glassfish.grizzly.http.Method;

import sun.misc.BASE64Encoder;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.core.util.StringKeyIgnoreCaseMultivaluedMap;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.resource.Singleton;

/**
 * @author Liang
 * 
 */
@Singleton
@Path("/")
public final class WebService {
	private final String webRoot;
	private final String webDefault;
	private final WebRequestHandler requestHandler;

	public WebService() {
		this.webRoot = RuntimeParameter.get("simulator.web.root", "web");
		this.webDefault = RuntimeParameter.get("simulator.web.default",
				"index.html");
		this.requestHandler = new WebRequestHandler();
	}

	@GET
	public Response getIndex(@Context UriInfo info, @Context HttpHeaders headers) {
		return this.load(this.webDefault);
	}

	@GET
	@Path("/{a}")
	@Produces(MediaType.TEXT_HTML)
	public String getA(@Context UriInfo info,
			@Context HttpHeaders headers) {
//		if (file.endsWith("html") || file.endsWith("xml")
//				|| file.endsWith("icon"))
//			return this.load(file);
//		return this.requestHandler.handle(Method.GET.getMethodString(),
//				info.getPath(), info.getQueryParameters(), null,
//				this.getSessionID(headers), this.getHost(info));
		StringBuffer buffer = new StringBuffer();
		String line;
		try{
			BufferedReader reader = new BufferedReader(new FileReader(new File(webRoot + "/" + info.getPath())));
			while((line = reader.readLine())!= null){
				buffer.append(line).append("\n");
			}
			reader.close();
		} catch (Exception e) {
		} finally {
		}
		return buffer.toString();
	}
	
	@GET
	@Path("log/{file}")
	public Response getLog( @Context UriInfo info ) {
		return this.loadLog(info.getPath());
	}

	@GET
	@Path("js/{a}")
	public Response getJs(@Context UriInfo info ) {
		return this.load(info.getPath());
	}

	@GET
	@Path("css/{a}")
	public Response getCss(@Context UriInfo info ) {
		return this.load(info.getPath());
	}
	
	@GET
	@Path("img/{a}")
	public Response getImage(@Context UriInfo info ) {
		return this.load(info.getPath());
	}
	
	@GET
	@Path("help/{a}")
	public Response getHelp(@Context UriInfo info ) {
		return this.load(info.getPath());
	}
	@GET
	@Path("{a}/{b}/{c}")
	public Response getC(@PathParam("a") String path1,
			@PathParam("b") String path2, @PathParam("c") String file,
			@Context UriInfo info, @Context HttpHeaders headers) {
		if (path1.equalsIgnoreCase("css") || path1.equalsIgnoreCase("js"))
			return this.load(info.getPath());
		return this.requestHandler.handle(Method.GET.getMethodString(),
				info.getPath(), info.getQueryParameters(), null,
				this.getSessionID(headers), this.getHost(info));
	}
	/*
	@GET
	@Path("{a}/{b}/{c}/{d}")
	public Response getD(@Context UriInfo info, @Context HttpHeaders headers) {
		return this.requestHandler.handle(Method.GET.getMethodString(),
				info.getPath(), info.getQueryParameters(), null,
				this.getSessionID(headers), this.getHost(info));
	}

	@GET
	@Path("{a}/{b}/{c}/{d}/{e}")
	public Response getE(@Context UriInfo info, @Context HttpHeaders headers) {
		return this.requestHandler.handle(Method.GET.getMethodString(),
				info.getPath(), info.getQueryParameters(), null,
				this.getSessionID(headers), this.getHost(info));
	}

	@GET
	@Path("{a}/{b}/{c}/{d}/{e}/{f}")
	public Response getF(@Context UriInfo info, @Context HttpHeaders headers) {
		return this.requestHandler.handle(Method.GET.getMethodString(),
				info.getPath(), info.getQueryParameters(), null,
				this.getSessionID(headers), this.getHost(info));
	}

	@GET
	@Path("{a}/{b}/{c}/{d}/{e}/{f}/{g}")
	public Response getG(@Context UriInfo info, @Context HttpHeaders headers) {
		return this.requestHandler.handle(Method.GET.getMethodString(),
				info.getPath(), info.getQueryParameters(), null,
				this.getSessionID(headers), this.getHost(info));
	}

	@GET
	@Path("{a}/{b}/{c}/{d}/{e}/{f}/{g}/{h}")
	public Response getH(@Context UriInfo info, @Context HttpHeaders headers) {
		return this.requestHandler.handle(Method.GET.getMethodString(),
				info.getPath(), info.getQueryParameters(), null,
				this.getSessionID(headers), this.getHost(info));
	}*/

	@POST
	@Path("license/import")
	@Produces({ MediaType.TEXT_PLAIN, MediaType.MULTIPART_FORM_DATA })
	@Consumes({ MediaType.TEXT_PLAIN, MediaType.MULTIPART_FORM_DATA })
	public Response uploadFile(
			@Context UriInfo info, @Context HttpHeaders headers,
			@FormDataParam("licensefile") InputStream uploadedInputStream,
			@FormDataParam("licensefile") FormDataContentDisposition fileDetail) {
		// read file
		String data = readUploadFile(uploadedInputStream);
		MultivaluedMap<String, String> formParams = new StringKeyIgnoreCaseMultivaluedMap<String>();
		formParams.add(fileDetail.getName(), data);		
		return this.requestHandler.handle(Method.POST.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	/*@POST
	@Path("{a}")
	public Response postA(@Context UriInfo info, @Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.POST.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@POST
	@Path("{a}/{b}")
	public Response postB(@Context UriInfo info, @Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.POST.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@POST
	@Path("{a}/{b}/{c}")
	public Response postC(@Context UriInfo info, @Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.POST.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@POST
	@Path("{a}/{b}/{c}/{d}")
	public Response postD(@Context UriInfo info, @Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.POST.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@POST
	@Path("{a}/{b}/{c}/{d}/{e}")
	public Response postE(@Context UriInfo info, @Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.POST.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@POST
	@Path("aaa/user/login/{name}/{password}")
	public Response login(@PathParam("name") String name,
			@PathParam("password") String password, @Context UriInfo info,
			@Context HttpHeaders headers) {
		String endpoint = "aaa/user/login";
		MultivaluedMap<String, String> formParameters = new MultivaluedMapImpl();
		List<String> names = new ArrayList<String>();
		names.add(name);
		formParameters.put("name", names);
		List<String> passwords = new ArrayList<String>();
		passwords.add(password);
		formParameters.put("password", passwords);
		return this.requestHandler.handle(Method.POST.getMethodString(),
				endpoint, null, formParameters, this.getSessionID(headers),
				this.getHost(info));
	}

	@POST
	@Path("{a}/{b}/{c}/{d}/{e}/{f}")
	public Response postF(@Context UriInfo info, @Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.POST.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@POST
	@Path("{a}/{b}/{c}/{d}/{e}/{f}/{g}")
	public Response postG(@Context UriInfo info, @Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.POST.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@POST
	@Path("{a}/{b}/{c}/{d}/{e}/{f}/{g}/{h}")
	public Response postH(@Context UriInfo info, @Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.POST.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@PUT
	@Path("{a}")
	public Response putA(@Context UriInfo info, @Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.PUT.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@PUT
	@Path("{a}/{b}")
	public Response putB(@Context UriInfo info, @Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.PUT.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@PUT
	@Path("{a}/{b}/{c}")
	public Response putC(@Context UriInfo info, @Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.PUT.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@PUT
	@Path("{a}/{b}/{c}/{d}")
	public Response putD(@Context UriInfo info, @Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.PUT.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@PUT
	@Path("{a}/{b}/{c}/{d}/{e}")
	public Response putE(@Context UriInfo info, @Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.PUT.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@PUT
	@Path("{a}/{b}/{c}/{d}/{e}/{f}")
	public Response putF(@Context UriInfo info, @Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.PUT.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@PUT
	@Path("{a}/{b}/{c}/{d}/{e}/{f}/{g}")
	public Response putG(@Context UriInfo info, @Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.PUT.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@PUT
	@Path("{a}/{b}/{c}/{d}/{e}/{f}/{g}/{h}")
	public Response putH(@Context UriInfo info, @Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.PUT.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@DELETE
	@Path("{a}")
	public Response deleteA(@Context UriInfo info,
			@Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.PUT.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@DELETE
	@Path("{a}/{b}")
	public Response deleteB(@Context UriInfo info,
			@Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.PUT.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@DELETE
	@Path("{a}/{b}/{c}")
	public Response deleteC(@Context UriInfo info,
			@Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.PUT.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@DELETE
	@Path("{a}/{b}/{c}/{d}")
	public Response deleteD(@Context UriInfo info,
			@Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.PUT.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@DELETE
	@Path("{a}/{b}/{c}/{d}/{e}")
	public Response deleteE(@Context UriInfo info,
			@Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.PUT.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@DELETE
	@Path("{a}/{b}/{c}/{d}/{e}/{f}")
	public Response deleteF(@Context UriInfo info,
			@Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.PUT.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@DELETE
	@Path("{a}/{b}/{c}/{d}/{e}/{f}/{g}")
	public Response deleteG(@Context UriInfo info,
			@Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.PUT.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}

	@DELETE
	@Path("{a}/{b}/{c}/{d}/{e}/{f}/{g}/{h}")
	public Response deleteH(@Context UriInfo info,
			@Context HttpHeaders headers,
			MultivaluedMap<String, String> formParams) {
		return this.requestHandler.handle(Method.PUT.getMethodString(),
				info.getPath(), info.getQueryParameters(), formParams,
				this.getSessionID(headers), this.getHost(info));
	}*/

	private String getSessionID(final HttpHeaders headers) {
		Cookie cookie = headers.getCookies().get("JSESSIONID");
		return cookie != null ? cookie.getValue() : null;
	}

	private String getHost(final UriInfo info) {
		URI uri = info.getBaseUri();
		return uri.getHost() + uri.getPort();
	}

	private String readUploadFile(InputStream uploadedInputStream) {
		String result = "";
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream(uploadedInputStream.available());
			int read = 0;
			byte[] buf = new byte[10240];
			while ((read = uploadedInputStream.read(buf)) != -1) {
				os.write(buf, 0, read);
			}
			BASE64Encoder encode = new BASE64Encoder();
			result = encode.encode(os.toByteArray());
			os.close();
			uploadedInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private Response loadLog(String name) {
		File file = new File(webRoot + "/../" + name);
		if (!file.exists())
			throw new WebApplicationException(404);
		return Response.ok(file, "text/html").build();
	}
	
	private Response load(String name) {
		File file = new File(webRoot + "/" + name);
		if (!file.exists())
			throw new WebApplicationException(404);
		String contentType = new MimetypesFileTypeMap().getContentType(file);
		if (name.toLowerCase().endsWith("html")) {
			contentType = "text/html";
		} else if (name.toLowerCase().endsWith("js")) {
			contentType = "text/javascript";
		} else if (name.toLowerCase().endsWith("css")) {
			contentType = "text/css";
		}
		return Response.ok(file, contentType).build();
	}
}
