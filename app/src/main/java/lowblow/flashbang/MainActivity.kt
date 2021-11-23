package lowblow.flashbang

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import java.lang.Thread.sleep
import java.util.*

class MainActivity : AppCompatActivity() {

    //이미지 버튼 바인딩
    private val imageButton : ImageButton by lazy {
        findViewById(R.id.flashButton)
    }

    //카메라 매니저, 플래시 관련 세팅
    private lateinit var camera: CameraManager
    private lateinit var cameraId: String

    //플래시를 깜빡거리기 위한 타이머
    var timer: Timer? = null

    //섬광탄 작동 여부
    private var isFlash = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        camera = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraId = camera.cameraIdList[0]

        initFlashBang()
    }

    override fun onPause() {
        super.onPause()

        isFlash = false
        setFlash()
    }

    private fun initFlashBang() {
        imageButton.setOnClickListener {
            isFlash = !isFlash

            setFlash()
        }
    }

    private fun setFlash() {
        if(isFlash) {
            imageButton.setBackgroundColor(getColor(R.color.black))
            imageButton.setColorFilter(Color.parseColor("#FFFFFFFF"), PorterDuff.Mode.SRC_IN)

            timer = Timer()
            timer!!.schedule(object : TimerTask() {
                override fun run() {
                    this@MainActivity.runOnUiThread {
                        camera.setTorchMode(cameraId, true)
                        sleep(100)
                        camera.setTorchMode(cameraId, false)
                    }
                }
            },0, 200)
        } else {
            timer?.cancel()

            imageButton.setBackgroundColor(getColor(R.color.white))
            imageButton.setColorFilter(Color.parseColor("#FF000000"), PorterDuff.Mode.SRC_IN)

            camera.setTorchMode(cameraId, false)
        }
    }
}