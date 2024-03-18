package com.example.hw.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.example.hw.R

class PullDownDumperLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs), View.OnTouchListener {

    // 初始化布局中的第一个子元素为Head
    private lateinit var headLayout: View

    // 阻尼的高度負值，即Head隐藏的部分
    private var dumperHeight: Int = 0

    // Head可滑動的高度負值
    private var headScrollRange: Int = 0

    // Head的布局参數，用於調整阻尼範圍
    private lateinit var headLayoutParams: MarginLayoutParams

    // 判斷是否為第一次布局，用於初始化時將阻尼範圍移除視圖
    private var isLayoutInitialized = false

    // 記錄觸發事件的Y座標
    private var lastTouchEventY: Float = 0f
    private var lastMoveY: Float = 0f

    // 標誌位，用於判斷Head是否處於動態調整狀態
    private var isAdjustingHeadLayout = false

    // 阻尼速度和睡眠时间，控制Head的回彈動畫
    private var dumperSpeed = -30
    private var sleepTime: Long = 20

    // 阻尼系數，控制拖拽的難易程度
    private var dumper = 2

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (!isLayoutInitialized && changed) {
            initializeLayout()
        }
    }

    // 初始化布局配置
    private fun initializeLayout() {
        headLayout = getChildAt(0)
        val resources = resources
        dumperHeight = -(resources.getDimension(R.dimen.top_bar_dumper_range).toInt())
        headScrollRange = dumperHeight - resources.getDimension(R.dimen.top_bar_scroll_view_height).toInt()
        headLayoutParams = headLayout.layoutParams as MarginLayoutParams
        headLayoutParams.topMargin = dumperHeight
        headLayout.layoutParams = headLayoutParams
        // 設置處監聽器
        headLayout.setOnTouchListener(this)
        getChildAt(1).setOnTouchListener(this)
        isLayoutInitialized = true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastTouchEventY = it.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val dy = (it.y - lastTouchEventY).toInt()
                    isAdjustingHeadLayout = when {
                        dy < 0 -> headLayoutParams.topMargin != headScrollRange
                        dy > 0 -> (0 until childCount).any { index ->
                            val child = getChildAt(index)
                            isViewAtTop(child)
                        }
                        else -> false
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    // 判斷childView是否皆已滑到頂部
    private fun isViewAtTop(view: View?): Boolean = when (view) {
        is RecyclerView -> !view.canScrollVertically(-1)
        is ScrollView, is NestedScrollView -> view.scrollY <= 0
        else -> false
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastMoveY = event.rawY
                isAdjustingHeadLayout = false // 中斷Head布局的動畫
            }
            MotionEvent.ACTION_MOVE -> {
                val currentY = event.rawY
                val dy = ((currentY - lastMoveY) / dumper).toInt()
                lastMoveY = currentY
                if (dy == 0) return canTouch(v)

                var topMargin = headLayoutParams.topMargin + dy
                topMargin = topMargin.coerceIn(headScrollRange..0)

                if (isAdjustingHeadLayout) {
                    headLayoutParams.topMargin = topMargin
                    headLayout.layoutParams = headLayoutParams
                }
            }
            else -> {
                isAdjustingHeadLayout = true
                if (headLayoutParams.topMargin in dumperHeight..0) {
                    MoveHeaderTask().execute()
                }
            }
        }
        return canTouch(v)
    }

    // 判斷觸摸是否可到 childView
    private fun canTouch(v: View?): Boolean = v != headLayout && isAdjustingHeadLayout

    // 異步任務，執行Head的回彈動畫
    @SuppressLint("StaticFieldLeak")
    private inner class MoveHeaderTask : AsyncTask<Boolean, Int, Int?>() {
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg opt: Boolean?): Int? {
            var topMargin = headLayoutParams.topMargin
            while (isAdjustingHeadLayout) {
                topMargin += dumperSpeed
                if (topMargin <= dumperHeight || topMargin >= 0) {
                    topMargin = dumperHeight
                    publishProgress(topMargin)
                    break
                }
                publishProgress(topMargin)
                SystemClock.sleep(sleepTime)
            }
            return null
        }

        @Deprecated("Deprecated in Java")
        override fun onProgressUpdate(vararg topMargin: Int?) {
            headLayoutParams.topMargin = topMargin[0] ?: 0
            headLayout.layoutParams = headLayoutParams
        }
    }
}