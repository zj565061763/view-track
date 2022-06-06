package com.sd.demo.vtrack

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.vtrack.databinding.ActivityMainBinding
import com.sd.lib.vtrack.ext.FPositionTracker
import com.sd.lib.vtrack.tracker.ViewTracker
import com.sd.lib.vtrack.updater.ViewUpdater
import com.sd.lib.vtrack.updater.impl.OnPreDrawUpdater

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val _binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
    }

    private val tracker: FPositionTracker by lazy {
        object : FPositionTracker() {
            override fun createTargetUpdater(): ViewUpdater {
                return OnPreDrawUpdater()
            }
        }.apply {
            // 设置源view
            setSource(_binding.viewSource)
            // 设置目标view
            setTarget(_binding.viewTarget)
            // 设置回调对象
            setCallback(object : ViewTracker.Callback() {
                /**
                 * 按照指定的位置[Position]追踪到target后回调，回调source相对于父布局的x和y值
                 *
                 * @param x      source相对于父布局的x值
                 * @param y      source相对于父布局的y值
                 * @param source 源view
                 * @param target 目标view
                 */
                override fun onUpdate(x: Int, y: Int, source: View, target: View) {
                    Log.i(TAG, "$x,$y")
                    source.layout(x, y, x + source.measuredWidth, y + source.measuredHeight)
                }
            })
        }
    }

    override fun onClick(v: View) {
        when (v) {
            _binding.btnStart -> {
                // 开始追踪
                tracker.start()
            }
            _binding.btnStop -> {
                // 停止追踪
                tracker.stop()
            }

            _binding.btnTopLeft -> tracker.setPosition(ViewTracker.Position.TopLeft)
            _binding.btnTopCenter -> tracker.setPosition(ViewTracker.Position.TopCenter)
            _binding.btnTopRight -> tracker.setPosition(ViewTracker.Position.TopRight)

            _binding.btnLeftCenter -> tracker.setPosition(ViewTracker.Position.LeftCenter)
            _binding.btnCenter -> tracker.setPosition(ViewTracker.Position.Center)
            _binding.btnRightCenter -> tracker.setPosition(ViewTracker.Position.RightCenter)

            _binding.btnBottomLeft -> tracker.setPosition(ViewTracker.Position.BottomLeft)
            _binding.btnBottomCenter -> tracker.setPosition(ViewTracker.Position.BottomCenter)
            _binding.btnBottomRight -> tracker.setPosition(ViewTracker.Position.BottomRight)
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}