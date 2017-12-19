package br.unicap.fullstack.minhamerenda.Service;

import okhttp3.*
import java.io.File
import java.util.concurrent.TimeUnit

class HttpService(private val timeout:Long = DEFAULT_TIME, private val timeunit:TimeUnit = TimeUnit.SECONDS ) {
    companion object {
        val DEFAULT_TIME:Long = 60
    }

    private val MEDIATYPE_JSON : MediaType? = MediaType.parse("application/json; charset=utf-8");
    private val MEDIATYPE_IMAGE_JPG : MediaType? = MediaType.parse("image/jpeg")
    private val MEDIATYPE_IMAGE_PNG : MediaType? = MediaType.parse("image/png")

    private val client : OkHttpClient  = OkHttpClient() //new OkHttpClient
            .newBuilder()
            .connectTimeout(timeout, timeunit)
            .writeTimeout(timeout, timeunit)
            .readTimeout(timeout, timeunit)
            .build();

    fun doGet(requestUrl : String) : Response {
        val request : Request = Request.Builder()
                .url(requestUrl)
                .build();
        val response : Response = this.client.newCall(request).execute();

        //return response.body()?.string();
        return response
    }

    fun doGet(requestUrl : String, headersMap: Map<String,String>) : Response {
        val headers: Headers = this.createHeaders(headersMap)
        val request : Request = Request.Builder()
                .headers(headers)
                .url(requestUrl)
                .build();
        val response : Response = this.client.newCall(request).execute();

        //return response.body()?.string();
        return response
    }


    fun doPost(requestUrl : String, jsonString : String) : Response {
        val body: RequestBody = RequestBody.create(this.MEDIATYPE_JSON, jsonString);
        val request: Request = Request.Builder()
                .url(requestUrl)
                .post(body)
                .build();

        val response: Response = this.client.newCall(request).execute();
        //return response.body()?.string();
        return response
    }

    fun doPost(requestUrl : String, jsonString : String, headersMap: Map<String, String>) : Response {
        val headers: Headers = this.createHeaders(headersMap)
        val body: RequestBody = RequestBody.create(this.MEDIATYPE_JSON, jsonString);
        val request: Request = Request.Builder()
                .headers(headers)
                .url(requestUrl)
                .post(body)
                .build();

        val response: Response = this.client.newCall(request).execute();
        //return response.body()?.string();
        return response
    }

    fun doPostMultipart(requestUrl : String, file : File, headersMap: Map<String, String>, id:String) : Response {
        var multipartBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("uploadedFile",file.name, RequestBody.create(this.MEDIATYPE_IMAGE_PNG, file))
                .addFormDataPart("avaliacaoid",id)
                .build()

        val headers: Headers = createHeaders(headersMap)

        val request: Request = Request.Builder()
                .headers(headers)
                .url(requestUrl)
                .post(multipartBody)
                .build()

        return this.client.newCall(request).execute()
    }

    private fun createHeaders(headersMap:Map<String, String>): Headers {
        val headersBuilder: Headers.Builder = Headers.of().newBuilder()

        /*val headers : Headers = Headers.of(
                "Appkey", appkey,
        "Authorization", token)*/
        for ((k, v) in headersMap) {
            headersBuilder.add(k, v)
        }
        return headersBuilder.build()
    }
}

