package cn.krisez.account.widget

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.*
import android.text.InputFilter.LengthFilter
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View.OnFocusChangeListener
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import cn.krisez.account.R

class XEditText @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {
    private var mSeparator //mSeparator，default is "".
            : String? = null
    private var disableClear = false// disable clear drawable. = false
    private var mClearDrawable: Drawable? = null
    private var mTogglePwdDrawable: Drawable? = null
    private var disableEmoji = false // disable emoji and some special symbol input. = false
    private var mShowPwdResId = 0
    private var mHidePwdResId = 0
    private var mTextChangeListener: OnTextChangeListener? = null
    private val mTextWatcher: TextWatcher
    private var mPreLength = 0
    private var hasFocused = false
    private var pattern: IntArray? =
            null// pattern to separate. e.g.: mSeparator = "-", pattern = [3,4,4] -> xxx-xxxx-xxxx
    private var intervals: IntArray? = null// indexes of separators.
    /* When you set pattern, it will automatically compute the max length of characters and separators,
     so you don't need to set 'maxLength' attr in your xml any more(it won't work).*/
    private var mMaxLength = Int.MAX_VALUE
    private var hasNoSeparator = false // true, the same as EditText. = false
    private var isPwdInputType = false
    private var isPwdShow = false
    private var mBitmap: Bitmap? = null
    private fun initAttrs(context: Context,attrs: AttributeSet?,defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.XEditText, defStyleAttr, 0)
        mSeparator = a.getString(R.styleable.XEditText_edittext_separator)
        if (mSeparator == null) {
            mSeparator = ""
        }
        if (mSeparator!!.isNotEmpty()) {
            val inputType = inputType
            if (inputType == 2 || inputType == 8194 || inputType == 4098) { // if inputType is number, it can't insert mSeparator.
                setInputType(InputType.TYPE_CLASS_PHONE)
            }
        }
        disableClear = a.getBoolean(R.styleable.XEditText_edittext_clearDrawable, false)
        val disableTogglePwdDrawable = a.getBoolean(R.styleable.XEditText_edittext_disableTogglePwdDrawable, false)
        if (!disableClear) {
            var cdId = a.getResourceId(R.styleable.XEditText_edittext_clearDrawable, -1)
            if (cdId == -1) cdId = R.drawable.x_et_svg_ic_clear_24dp
            mClearDrawable = AppCompatResources.getDrawable(context, cdId)
            if (mClearDrawable != null) {
                mClearDrawable!!.setBounds(
                        0, 0, mClearDrawable!!.intrinsicWidth,
                        mClearDrawable!!.intrinsicHeight
                )
                if (cdId == R.drawable.x_et_svg_ic_clear_24dp) DrawableCompat.setTint(
                        mClearDrawable!!,
                        currentHintTextColor
                )
            }
        }
        val inputType = inputType
        if (!disableTogglePwdDrawable && (inputType == 129 || inputType == 145 || inputType == 18 || inputType == 225)) {
            isPwdInputType = true
            isPwdShow = inputType == 145
            //            mMaxLength = 20;
            mShowPwdResId = a.getResourceId(R.styleable.XEditText_edittext_showPwdDrawable, -1)
            mHidePwdResId = a.getResourceId(R.styleable.XEditText_edittext_hidePwdDrawable, -1)
            if (mShowPwdResId == -1) mShowPwdResId = R.drawable.x_et_svg_ic_show_password_24dp
            if (mHidePwdResId == -1) mHidePwdResId = R.drawable.x_et_svg_ic_hide_password_24dp
            val tId = if (isPwdShow) mShowPwdResId else mHidePwdResId
            mTogglePwdDrawable = AppCompatResources.getDrawable(context, tId)
            if (mShowPwdResId == R.drawable.x_et_svg_ic_show_password_24dp ||
                    mHidePwdResId == R.drawable.x_et_svg_ic_hide_password_24dp
            ) {
                DrawableCompat.setTint(mTogglePwdDrawable!!, currentHintTextColor)
            }
            mTogglePwdDrawable!!.setBounds(
                    0, 0, mTogglePwdDrawable!!.intrinsicWidth,
                    mTogglePwdDrawable!!.intrinsicHeight
            )
            var cdId = a.getResourceId(R.styleable.XEditText_edittext_clearDrawable, -1)
            if (cdId == -1) cdId = R.drawable.x_et_svg_ic_clear_24dp
            if (!disableClear) {
                mBitmap = getBitmapFromVectorDrawable(
                        context, cdId,
                        cdId == R.drawable.x_et_svg_ic_clear_24dp
                ) // clearDrawable
            }
        }
        disableEmoji = a.getBoolean(R.styleable.XEditText_edittext_disableEmoji, true)
        a.recycle()
    }

    private fun getBitmapFromVectorDrawable(context: Context,drawableId: Int,tint: Boolean): Bitmap? {
        val drawable = AppCompatResources.getDrawable(context, drawableId) ?: return null
        if (tint) DrawableCompat.setTint(drawable, currentHintTextColor)
        val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (hasFocused && mBitmap != null && isPwdInputType && !isTextEmpty) {
            val left = measuredWidth - paddingRight -
                    mTogglePwdDrawable!!.intrinsicWidth - mBitmap!!.width - dp2px(10)
            val top =
                    (height - paddingBottom - paddingTop) / 2 - mBitmap!!.height / 2
            canvas.drawBitmap(mBitmap!!, left.toFloat(), top + paddingTop.toFloat(), null)
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            performClick()
        }
        if (hasFocused && isPwdInputType && event.action == MotionEvent.ACTION_UP) {
            val w = mTogglePwdDrawable!!.intrinsicWidth
            val h = mTogglePwdDrawable!!.intrinsicHeight
            val top = measuredHeight - h shr 1
            var right = measuredWidth - paddingRight
            var isAreaX = event.x <= right && event.x >= right - w
            val isAreaY = event.y >= top && event.y <= top + h
            if (isAreaX && isAreaY) {
                isPwdShow = !isPwdShow
                transformationMethod = if (isPwdShow) {
                    HideReturnsTransformationMethod.getInstance()
                } else {
                    PasswordTransformationMethod.getInstance()
                }
                setSelection(selectionStart, selectionEnd)
                mTogglePwdDrawable = ContextCompat.getDrawable(
                        context,
                        if (isPwdShow) mShowPwdResId else mHidePwdResId
                )
                if (mShowPwdResId == R.drawable.x_et_svg_ic_show_password_24dp ||
                        mHidePwdResId == R.drawable.x_et_svg_ic_hide_password_24dp
                ) {
                    DrawableCompat.setTint(mTogglePwdDrawable!!, currentHintTextColor)
                }
                mTogglePwdDrawable!!.setBounds(
                        0, 0, mTogglePwdDrawable!!.intrinsicWidth,
                        mTogglePwdDrawable!!.intrinsicHeight
                )
                setCompoundDrawables(
                        compoundDrawables[0], compoundDrawables[1],
                        mTogglePwdDrawable, compoundDrawables[3]
                )
                invalidate()
            }
            if (!disableClear) {
                right -= w + dp2px(10)
                isAreaX = event.x <= right && event.x >= right - mBitmap!!.width
                if (isAreaX && isAreaY) {
                    error = null
                    setText("")
                }
            }
        }
        if (hasFocused && !disableClear && !isPwdInputType && event.action == MotionEvent.ACTION_UP) {
            val rect = mClearDrawable!!.bounds
            val rectW = rect.width()
            val rectH = rect.height()
            val top = measuredHeight - rectH shr 1
            val right = measuredWidth - paddingRight
            val isAreaX = event.x <= right && event.x >= right - rectW
            val isAreaY = event.y >= top && event.y <= top + rectH
            if (isAreaX && isAreaY) {
                error = null
                setText("")
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onTextContextMenuItem(id: Int): Boolean {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (id == android.R.id.cut || id == android.R.id.copy) { // catch CUT or COPY ops
            super.onTextContextMenuItem(id)
            val clip = clipboardManager.primaryClip
            val item = clip!!.getItemAt(0)
            if (item != null && item.text != null) {
                val s = item.text.toString().replace(mSeparator!!, "")
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, s))
                return true
            }
        } else if (id == android.R.id.paste) { // catch PASTE ops
            val clip = clipboardManager.primaryClip
            val item = clip!!.getItemAt(0)
            if (item != null && item.text != null) {
                setTextToSeparate((text.toString() + item.text.toString()).replace(mSeparator!!,""))
                return true
            }
        }
        return super.onTextContextMenuItem(id)
    }

    // =========================== MyTextWatcher ================================
    private inner class MyTextWatcher : TextWatcher {
        override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
        ) {
            mPreLength = s.length
            if (mTextChangeListener != null) {
                mTextChangeListener!!.beforeTextChanged(s, start, count, after)
            }
        }

        override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
        ) {
            if (mTextChangeListener != null) {
                mTextChangeListener!!.onTextChanged(s, start, before, count)
            }
        }

        override fun afterTextChanged(s: Editable) {
            if (mTextChangeListener != null) {
                mTextChangeListener!!.afterTextChanged(s)
            }
            val currLength = s.length
            if (hasNoSeparator) {
                mMaxLength = currLength
            }
            markerFocusChangeLogic()
            if (currLength > mMaxLength) {
                text!!.delete(currLength - 1, currLength)
                return
            }
            if (pattern == null) {
                return
            }
            for (i in pattern!!.indices) {
                if (currLength - 1 == intervals?.get(i) ?: 0) {
                    if (currLength > mPreLength) { // inputting
                        if (currLength < mMaxLength) {
                            removeTextChangedListener(mTextWatcher)
                            text!!.insert(currLength - 1, mSeparator)
                        }
                    } else if (mPreLength <= mMaxLength) { // deleting
                        removeTextChangedListener(mTextWatcher)
                        text!!.delete(currLength - 1, currLength)
                    }
                    addTextChangedListener(mTextWatcher)
                    break
                }
            }
        }
    }

    private fun markerFocusChangeLogic() {
        if (!hasFocused || isTextEmpty && !isPwdInputType) {
            setCompoundDrawables(
                    compoundDrawables[0], compoundDrawables[1],
                    null, compoundDrawables[3]
            )
            if (!isTextEmpty && isPwdInputType) {
                invalidate()
            }
        } else {
            if (isPwdInputType) {
                if (mShowPwdResId == R.drawable.x_et_svg_ic_show_password_24dp ||
                        mHidePwdResId == R.drawable.x_et_svg_ic_hide_password_24dp
                ) {
                    DrawableCompat.setTint(mTogglePwdDrawable!!, currentHintTextColor)
                }
                setCompoundDrawables(
                        compoundDrawables[0], compoundDrawables[1],
                        mTogglePwdDrawable, compoundDrawables[3]
                )
            } else if (!isTextEmpty && !disableClear) {
                setCompoundDrawables(
                        compoundDrawables[0], compoundDrawables[1],
                        mClearDrawable, compoundDrawables[3]
                )
            }
        }
    }

    private val isTextEmpty: Boolean = text.toString().trim { it <= ' ' }.isEmpty()

    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
                Resources.getSystem().displayMetrics
        ).toInt()
    }

    /**
     * set customize separator
     */
    fun setSeparator(separator: String) {
        mSeparator = separator
        if (mSeparator!!.length > 0) {
            val inputType = inputType
            if (inputType == 2 || inputType == 8194 || inputType == 4098) { // if inputType is number, it can't insert mSeparator.
                setInputType(InputType.TYPE_CLASS_PHONE)
            }
        }
    }

    /**
     * set customize pattern
     *
     * @param pattern   e.g. pattern:{4,4,4,4}, separator:"-" to xxxx-xxxx-xxxx-xxxx
     * @param separator separator
     */
    fun setPattern(pattern: IntArray, separator: String) {
        setSeparator(separator)
        setPattern(pattern)
    }

    /**
     * set customize pattern
     *
     * @param pattern e.g. pattern:{4,4,4,4}, separator:"-" to xxxx-xxxx-xxxx-xxxx
     */
    fun setPattern(pattern: IntArray) {
        this.pattern = pattern
        intervals = IntArray(pattern.size)
        var count = 0
        var sum = 0
        for (i in pattern.indices) {
            sum += pattern[i]
            intervals!![i] = sum + count
            if (i < pattern.size - 1) {
                count += mSeparator!!.length
            }
        }
        mMaxLength = intervals!![intervals!!.size - 1]
        val filters = arrayOfNulls<InputFilter>(1)
        filters[0] = LengthFilter(mMaxLength)
        setFilters(filters)
    }

    /**
     * set CharSequence to separate
     */
    fun setTextToSeparate(c: CharSequence) {
        if (c.length == 0) {
            return
        }
        setText("")
        for (i in 0 until c.length) {
            append(c.subSequence(i, i + 1))
        }
    }

    /**
     * Get text without separators.
     *
     *
     * Deprecated, use [.getTrimmedString] instead.
     */
    @get:Deprecated("")
    val nonSeparatorText: String
        get() = text.toString().replace(mSeparator!!.toRegex(), "")

    /**
     * Get text String having been trimmed.
     */
    val trimmedString: String
        get() = if (hasNoSeparator) {
            text.toString().trim { it <= ' ' }
        } else {
            text.toString().replace(mSeparator!!.toRegex(), "").trim { it <= ' ' }
        }

    /**
     * @return has separator or not
     */
    fun hasNoSeparator(): Boolean {
        return hasNoSeparator
    }

    /**
     * @param hasNoSeparator true, has no separator, the same as EditText
     */
    fun setHasNoSeparator(hasNoSeparator: Boolean) {
        this.hasNoSeparator = hasNoSeparator
        if (hasNoSeparator) {
            mSeparator = ""
        }
    }

    /**
     * set true to disable Emoji and special symbol
     *
     * @param disableEmoji true: disable emoji;
     * false: enable emoji
     */
    fun setDisableEmoji(disableEmoji: Boolean) {
        this.disableEmoji = disableEmoji
        filters = if (disableEmoji) {
            arrayOf<InputFilter>(EmojiExcludeFilter())
        } else {
            arrayOfNulls(0)
        }
    }

    /**
     * the same as EditText.addOnTextChangeListener(TextWatcher textWatcher)
     */
    fun setOnXTextChangeListener(listener: OnTextChangeListener?) {
        mTextChangeListener = listener
    }

    interface OnTextChangeListener {
        fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
        )

        fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
        )

        fun afterTextChanged(s: Editable?)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("save_instance", super.onSaveInstanceState())
        bundle.putString("separator", mSeparator)
        bundle.putIntArray("pattern", pattern)
        bundle.putBoolean("hasNoSeparator", hasNoSeparator)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            val bundle = state
            mSeparator = bundle.getString("separator")
            pattern = bundle.getIntArray("pattern")
            hasNoSeparator = bundle.getBoolean("hasNoSeparator")
            if (pattern != null) {
                setPattern(pattern!!)
            }
            super.onRestoreInstanceState(bundle.getParcelable("save_instance"))
            return
        }
        super.onRestoreInstanceState(state)
    }

    /**
     * disable emoji and special symbol input
     */
    private inner class EmojiExcludeFilter : InputFilter {
        override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
            for (i in start until end) {
                val type = Character.getType(source[i])
                if (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()) {
                    return ""
                }
            }
            return null
        }
    }

    init {
        initAttrs(context, attrs, defStyleAttr)
        if (disableEmoji) {
            val filters = filters
            val newFilters = arrayOfNulls<InputFilter>(filters.size + 1)
            System.arraycopy(filters, 0, newFilters, 0, filters.size)
            newFilters[filters.size] = EmojiExcludeFilter()
            setFilters(newFilters)
            //            setFilters(new InputFilter[]{new EmojiExcludeFilter()});
        }
        mTextWatcher = MyTextWatcher()
        addTextChangedListener(mTextWatcher)
        onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            hasFocused = hasFocus
            markerFocusChangeLogic()
        }
        compoundDrawablePadding = dp2px(5)
    }
}