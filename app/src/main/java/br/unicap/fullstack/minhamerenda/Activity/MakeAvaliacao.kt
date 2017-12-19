/*
 * MakeAvaliacao.kt
 * Permite selecionar uma foto da galeria e escolher uma pontuação 0 - 5.
 * Envia a avaliação pro servidor
 *
 */

package br.unicap.fullstack.minhamerenda.Activity

import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.unicap.fullstack.minhamerenda.Data.Escola
import br.unicap.fullstack.minhamerenda.Model.Usuario
import br.unicap.fullstack.minhamerenda.R
import br.unicap.fullstack.minhamerenda.Service.AvaliacaoRepository
import kotlinx.android.synthetic.main.activity_make_avaliacao.*
import org.json.JSONObject
import java.io.Serializable
import java.net.SocketTimeoutException
import kotlin.concurrent.thread

class MakeAvaliacao : AppCompatActivity() {

    val SELECTED_PIC: Int = 1 //Adnroid
    var imageFilePath:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_avaliacao)

        var bundle: Bundle = intent.extras
        val escola : Escola = bundle.getSerializable("escola") as Escola
        val user: Usuario = bundle.getSerializable("user") as Usuario

        this.txtEscola.text = escola.escolaNome

        this.imgView.setOnClickListener {_ ->
            val intent: Intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, this.SELECTED_PIC)
        }

        //Depois do POST retorna a activity Main
        fabLocalize.setOnClickListener { view ->
            val _imageFilePath:String = imageFilePath ?: ""
            if(!_imageFilePath.isNullOrEmpty()) {
                Snackbar.make(view, "Enviando Avaliação. Aguarde.", Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
                enviarAvaliacao(escola, user, _imageFilePath)
            }
            else {
                Snackbar.make(view, "Selecione uma Imagem", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }
        }
    }

    private fun enviarAvaliacao(escola:Escola, user:Usuario, filePath:String) {
        //Making Avaliacao
        val avaliacaoJson:JSONObject = JSONObject()
                .put("escola",JSONObject().put("id",escola.id))
                .put("usuario",JSONObject().put("id", user.id))
                .put("pontuacao", this.starPontuacaoPost.rating)

        thread {
            try {
                if(AvaliacaoRepository.postAvaliacaoToServer(avaliacaoJson, filePath, this@MakeAvaliacao))
                    Log.i("PostAvaliação","Avaliação Inserida !")
                else
                    Log.i("PostAvaliação","Avaliação Não Inserida !")

                //Retorna ao MainActivity
                var bundle = Bundle()
                bundle.putSerializable("escola", escola as Serializable)
                startActivity(Intent(this, MainActivity::class.java).putExtras(bundle))
                finish()

            }catch (e: IllegalArgumentException){
                Snackbar.make(this.makeAvaliacaoView, e.message.toString(), Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
            }catch (b: SocketTimeoutException){
                Snackbar.make(this.makeAvaliacaoView, b.message.toString(), Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            this.SELECTED_PIC -> {
                imageFilePath = handleImage(data)
            }
            else -> {
                Log.i("onActivityResult", "RequestCode != SELECTED_PIC")
            }
        }
    }

    private fun handleImage(data:Intent?): String {
        val selectedImage: Uri? = data?.getData() //selectedImage = URI da imagem (content:// schema)
        val filePathColumn:Array<String> = arrayOf(MediaStore.Images.Media.DATA) //Path to the file on disk
        val cursor:Cursor = this.contentResolver.query(selectedImage, filePathColumn, null, null, null) //query given URI. Return Cursor for the ResultSet
        cursor.moveToFirst()

        val columnIndex:Int = cursor.getColumnIndex(filePathColumn[0]) //retorna o indice da coluna para o nome da coluna dada
        val filePath:String = cursor.getString(columnIndex) //Retorna o valor da coluna passada como uma String
        cursor.close()

        /* -- debug -- */
        Log.i("[selectedImage]", selectedImage.toString()) // content://media/external/images/media/21
        Log.i("[filePathColumn]", filePathColumn[0]) // _data
        Log.i("[MediaStore]", MediaStore.Images.Media.DATA) // _data
        Log.i("[columnIndex]", columnIndex.toString()) //0
        Log.i("[filePath]", filePath) // /storage/sdcard/Download/logo-small-914ab388ea8891cdeca192ea4d7c1cd611d05b687de1b8a5f4a25daa01e512d3d83ffe3182654e826da7ddc4d363ae98be0fc9df647d317470ba492a458c7e7f.png

        val bitmap:Bitmap = BitmapFactory.decodeFile(filePath)
        val drawable:Drawable = BitmapDrawable(bitmap)
        runOnUiThread({
            this.imgView.setImageDrawable(drawable)
        })

        return filePath
    }
}
