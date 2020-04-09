package com.education.core_api.presentation.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView призванный очищать ссылку на adapter,
 * когда ViewParent более не активен и подлежит сборке GC.
 * Это убирает MemoryLeak связанные с неявной утечкой Context.
 */
open class AutoCleanableRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // Очищаем адаптер, чтобы он освободил ссылку на RecyclerView/Context
        // Второй параметр возвращает ненужные вьюхи в RecyclerViewPool
        if (adapter != null) {
            swapAdapter(null, true)
        }
    }
}