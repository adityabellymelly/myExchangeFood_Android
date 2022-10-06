package com.exchange.user.module.utility_module

import android.content.Context
import android.util.AttributeSet
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import java.lang.reflect.Field

class NoMenuEditText : AppCompatEditText {

    /**
     * This is a replacement method for the base TextView class' method of the same name. This
     * method is used in hidden class android.widget.Editor to determine whether the PASTE/REPLACE popup
     * appears when triggered from the text insertion handle. Returning false forces this window
     * to never appear.
     *
     * @return false
     */
    fun canPaste(): Boolean {
        return false
    }



    /**
     * This is a replacement method for the base TextView class' method of the same name. This method
     * is used in hidden class android.widget.Editor to determine whether the PASTE/REPLACE popup
     * appears when triggered from the text insertion handle. Returning false forces this window
     * to never appear.
     *
     * @return false
     */
    override fun isSuggestionsEnabled(): Boolean {
        return false
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    private fun init() {
        this.customSelectionActionModeCallback = ActionModeCallbackInterceptor()
        this.isLongClickable = false
    }

    override fun onTextContextMenuItem(id: Int): Boolean {
        when (id) {
            android.R.id.paste,
            android.R.id.pasteAsPlainText -> return false
        }
        return super.onTextContextMenuItem(id)
    }

    override fun getSelectionStart(): Int {
        for (element in Thread.currentThread().stackTrace) {
            if (element.methodName == "canPaste") {
                return -1
            }
            if (element.methodName == "canCut") {
                return -1
            }
            if (element.methodName == "canCopy") {
                return -1
            }
        }
        return super.getSelectionStart()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            // setInsertionDisabled when user touches the view
            setInsertionDisabled()
        }
        return super.onTouchEvent(event)
    }

    /**
     * This method sets TextView#Editor#mInsertionControllerEnabled field to false
     * to return false from the Editor#hasInsertionController() method to PREVENT showing
     * of the insertionController from EditText
     * The Editor#hasInsertionController() method is called in  Editor#onTouchUpEvent(MotionEvent event) method.
     */
    private fun setInsertionDisabled() {
        try {
            val editorField: Field = TextView::class.java.getDeclaredField("mEditor")
            editorField.isAccessible = true
            val editorObject: Any? = editorField.get(this)
            val editorClass = Class.forName("android.widget.Editor")
            val mInsertionControllerEnabledField: Field =
                editorClass.getDeclaredField("mInsertionControllerEnabled")
            mInsertionControllerEnabledField.isAccessible = true
            mInsertionControllerEnabledField.set(editorObject, false)
        } catch (ignored: Exception) {
            // ignore exception here
        }
    }

    /**
     * Prevents the action bar (top horizontal bar with cut, copy, paste, etc.) from appearing
     * by intercepting the callback that would cause it to be created, and returning false.
     */
    private inner class ActionModeCallbackInterceptor : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode) {}
    }
}