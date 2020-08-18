package es.ccrr.aloloco.ui.components

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.database.DataSetObserver
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.*
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.util.Pair
import androidx.viewpager.widget.ViewPager
import es.ccrr.aloloco.R

class MyPagerSlidingTabStrip @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attributeSet, defStyleAttr) {

    val DEF_VALUE_TAB_TEXT_ALPHA = 150
    private val ANDROID_ATTRS = intArrayOf(
        android.R.attr.textColorPrimary,
        android.R.attr.padding,
        android.R.attr.paddingLeft,
        android.R.attr.paddingRight
    )

    //These indexes must be related with the ATTR array above
    private val TEXT_COLOR_PRIMARY = 0
    private val PADDING_INDEX = 1
    private val PADDING_LEFT_INDEX = 2
    private val PADDING_RIGHT_INDEX = 3

    private var mTabsContainer: LinearLayout = LinearLayout(context)
    private lateinit var mTabLayoutParams: LinearLayout.LayoutParams

    private val mAdapterObserver = PagerAdapterObserver()
    private val mPageListener = PageListener()
    private var mTabReselectedListener: OnTabReselectedListener? = null
    var mDelegatePageListener: ViewPager.OnPageChangeListener? = null
    private var mPager: ViewPager? = null

    private var mTabCount: Int = 0

    private var mCurrentPosition = 0
    private var mCurrentPositionOffset = 0f

    private var mRectPaint: Paint = Paint()
    private var mDividerPaint: Paint = Paint()

    private var mIndicatorColor: Int = 0
    private var mIndicatorHeight = 2

    private var mUnderlineHeight = 0
    private var mUnderlineColor: Int = 0

    private var mDividerWidth = 0
    private var mDividerPadding = 0
    private var mDividerColor: Int = 0

    private var mTabPadding = 12
    private var mTabTextSize = 14
    private var mTabTextColor: ColorStateList? = null

    private var mPaddingLeft = 0
    private var mPaddingRight = 0

    private var isExpandTabs = false
    private var isCustomTabs: Boolean = false
    private var isPaddingMiddle = false
    private var isTabTextAllCaps = true

    private var mTabTextTypeface: Typeface? = null
    private var mTabTextTypefaceStyle = Typeface.BOLD

    private var mScrollOffset: Int = 0
    private var mLastScrollX = 0

    private var mTabBackgroundResId = R.drawable.psts_background_tab

    init {
        isFillViewport = true
        setWillNotDraw(false)
        mTabsContainer = LinearLayout(context)
        mTabsContainer.orientation = LinearLayout.HORIZONTAL
        mTabsContainer.gravity = Gravity.CENTER_HORIZONTAL
        addView(mTabsContainer)

        mRectPaint = Paint()
        mRectPaint.isAntiAlias = true
        mRectPaint.style = Paint.Style.FILL

        val dm = resources.displayMetrics
        mScrollOffset =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mScrollOffset.toFloat(), dm)
                .toInt()
        mIndicatorHeight =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mIndicatorHeight.toFloat(), dm)
                .toInt()
        mUnderlineHeight =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mUnderlineHeight.toFloat(), dm)
                .toInt()
        mDividerPadding =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDividerPadding.toFloat(), dm)
                .toInt()
        mTabPadding =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTabPadding.toFloat(), dm)
                .toInt()
        mDividerWidth =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDividerWidth.toFloat(), dm)
                .toInt()
        mTabTextSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTabTextSize.toFloat(), dm)
                .toInt()

        mDividerPaint = Paint()
        mDividerPaint.isAntiAlias = true
        mDividerPaint.strokeWidth = mDividerWidth.toFloat()

        // get system attrs for container
        var a = context.obtainStyledAttributes(attributeSet, ANDROID_ATTRS)
        val textPrimaryColor =
            a.getColor(TEXT_COLOR_PRIMARY, resources.getColor(android.R.color.black))
        mUnderlineColor = textPrimaryColor
        mDividerColor = textPrimaryColor
        mIndicatorColor = textPrimaryColor
        val padding = a.getDimensionPixelSize(PADDING_INDEX, 0)
        mPaddingLeft = if (padding > 0) padding else a.getDimensionPixelSize(PADDING_LEFT_INDEX, 0)
        mPaddingRight =
            if (padding > 0) padding else a.getDimensionPixelSize(PADDING_RIGHT_INDEX, 0)
        a.recycle()

        var tabTextTypefaceName = "sans-serif"
        // Use Roboto Medium as the default typeface from API 21 onwards
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabTextTypefaceName = "sans-serif-medium"
            mTabTextTypefaceStyle = Typeface.NORMAL
        }

        // get custom attrs for tabs and container
        a = context.obtainStyledAttributes(
            attributeSet, R.styleable.PagerSlidingTabStrip
        )
        mIndicatorColor = a.getColor(
            R.styleable.PagerSlidingTabStrip_pstsIndicatorColor,
            mIndicatorColor
        )
        mIndicatorHeight = a.getDimensionPixelSize(
            R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight,
            mIndicatorHeight
        )
        mUnderlineColor = a.getColor(
            R.styleable.PagerSlidingTabStrip_pstsUnderlineColor,
            mUnderlineColor
        )
        mUnderlineHeight = a.getDimensionPixelSize(
            R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight,
            mUnderlineHeight
        )
        mDividerColor = a.getColor(
            R.styleable.PagerSlidingTabStrip_pstsDividerColor,
            mDividerColor
        )
        mDividerWidth = a.getDimensionPixelSize(
            R.styleable.PagerSlidingTabStrip_pstsDividerWidth,
            mDividerWidth
        )
        mDividerPadding = a.getDimensionPixelSize(
            R.styleable.PagerSlidingTabStrip_pstsDividerPadding,
            mDividerPadding
        )
        isExpandTabs = a.getBoolean(
            R.styleable.PagerSlidingTabStrip_pstsShouldExpand,
            isExpandTabs
        )
        mScrollOffset = a.getDimensionPixelSize(
            R.styleable.PagerSlidingTabStrip_pstsScrollOffset,
            mScrollOffset
        )
        isPaddingMiddle = a.getBoolean(
            R.styleable.PagerSlidingTabStrip_pstsPaddingMiddle,
            isPaddingMiddle
        )
        mTabPadding = a.getDimensionPixelSize(
            R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight,
            mTabPadding
        )
        mTabBackgroundResId = a.getResourceId(
            R.styleable.PagerSlidingTabStrip_pstsTabBackground,
            mTabBackgroundResId
        )
        mTabTextSize = a.getDimensionPixelSize(
            R.styleable.PagerSlidingTabStrip_pstsTabTextSize,
            mTabTextSize
        )
        mTabTextColor =
            if (a.hasValue(R.styleable.PagerSlidingTabStrip_pstsTabTextColor)) a.getColorStateList(
                R.styleable.PagerSlidingTabStrip_pstsTabTextColor
            ) else null
        mTabTextTypefaceStyle = a.getInt(
            R.styleable.PagerSlidingTabStrip_pstsTabTextStyle,
            mTabTextTypefaceStyle
        )
        isTabTextAllCaps = a.getBoolean(
            R.styleable.PagerSlidingTabStrip_pstsTabTextAllCaps,
            isTabTextAllCaps
        )
        val tabTextAlpha = a.getInt(
            R.styleable.PagerSlidingTabStrip_pstsTabTextAlpha,
            DEF_VALUE_TAB_TEXT_ALPHA
        )
        val fontFamily =
            a.getString(R.styleable.PagerSlidingTabStrip_pstsTabTextFontFamily)
        a.recycle()

        //Tab text color selector
        if (mTabTextColor == null) {
            mTabTextColor = createColorStateList(
                textPrimaryColor,
                textPrimaryColor,
                Color.argb(
                    tabTextAlpha,
                    Color.red(textPrimaryColor),
                    Color.green(textPrimaryColor),
                    Color.blue(textPrimaryColor)
                )
            )
        }

        //Tab text typeface and style
        if (fontFamily != null) {
            tabTextTypefaceName = fontFamily
        }
        mTabTextTypeface = Typeface.create(tabTextTypefaceName, mTabTextTypefaceStyle)

        //Bottom padding for the tabs container parent view to show indicator and underline
        setTabsContainerParentViewPaddings()

        //Configure tab's container LayoutParams for either equal divided space or just wrap tabs
        mTabLayoutParams = if (isExpandTabs)
            LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT, 1.0f)
        else
            LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
    }

    private fun setTabsContainerParentViewPaddings() {
        val bottomMargin =
            if (mIndicatorHeight >= mUnderlineHeight) mIndicatorHeight else mUnderlineHeight
        setPadding(paddingLeft, paddingTop, paddingRight, bottomMargin)
    }

    fun setViewPager(pager: ViewPager, parentWeight: Float, weight: Float) {
        this.mPager = pager
        checkNotNull(pager.adapter) { "ViewPager does not have adapter instance." }

        isCustomTabs = pager.adapter is CustomTabProvider
        pager.setOnPageChangeListener(mPageListener)
        pager.adapter!!.registerDataSetObserver(mAdapterObserver)
        mAdapterObserver.isAttached = true
        notifyDataSetChanged()
        if (parentWeight > 0.0f)
            setWeight(parentWeight, weight)
    }

    fun notifyDataSetChanged() {
        mTabsContainer.removeAllViews()
        mTabCount = mPager?.adapter!!.count
        var tabView: View
        for (i in 0 until mTabCount) {
            if (isCustomTabs) {
                tabView = (mPager?.adapter as CustomTabProvider).getCustomTabView(this, i)
            } else {
                tabView = LayoutInflater.from(context)
                    .inflate(R.layout.psts_tab, this, false)
            }

            val title = mPager?.adapter!!.getPageTitle(i)
            addTab(i, title, tabView)
        }

        updateTabStyles()
    }

    private fun addTab(position: Int, title: CharSequence?, tabView: View) {
        val textView = tabView.findViewById(R.id.psts_tab_title) as TextView
        if (textView != null) {
            if (title != null) textView.text = title
        }

        tabView.isFocusable = true
        tabView.setOnClickListener { v ->
            if (mPager?.currentItem != position) {
                val tab = mTabsContainer.getChildAt(mPager!!.currentItem)
                unSelect(tab)
                mPager?.currentItem = position
            } else if (mTabReselectedListener != null) {
                mTabReselectedListener?.onTabReselected(position)
            }
        }

        mTabsContainer.addView(tabView, position, mTabLayoutParams)
    }

    //    private void updateTabStyles(float parentWeight, float itemWeight) {
    fun updateTabStyles() {
        for (i in 0 until mTabCount) {
            val v = mTabsContainer.getChildAt(i)
            v.setBackgroundResource(mTabBackgroundResId)
            v.setPadding(mTabPadding, v.paddingTop, mTabPadding, v.paddingBottom)
            val tab_title =
                v.findViewById(R.id.psts_tab_title) as TextView
            if (tab_title != null) {
                tab_title.setTextColor(mTabTextColor)
                tab_title.setTypeface(mTabTextTypeface, mTabTextTypefaceStyle)
                tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabTextSize.toFloat())
                // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
                // pre-ICS-build
                if (isTabTextAllCaps)
                    tab_title.text =
                        tab_title.text.toString().toUpperCase(resources.configuration.locale)
            }
        }
    }

    private fun scrollToChild(position: Int, offset: Int) {
        if (mTabCount == 0) {
            return
        }

        var newScrollX = mTabsContainer.getChildAt(position).left + offset
        if (position > 0 || offset > 0) {
            //Half screen offset.
            //- Either tabs start at the middle of the view scrolling straight away
            //- Or tabs start at the begging (no padding) scrolling when indicator gets
            //  to the middle of the view width
            newScrollX -= mScrollOffset
            val lines = getIndicatorCoordinates()
            newScrollX += ((lines.second!! - lines.first!!) / 2).toInt()
        }

        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX
            scrollTo(newScrollX, 0)
        }
    }

    private fun getIndicatorCoordinates(): Pair<Float, Float> {
        // default: line below current tab
        val currentTab = mTabsContainer.getChildAt(mCurrentPosition)
        var lineLeft = currentTab.left.toFloat()
        var lineRight = currentTab.right.toFloat()
        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (mCurrentPositionOffset > 0f && mCurrentPosition < mTabCount - 1) {
            val nextTab = mTabsContainer.getChildAt(mCurrentPosition + 1)
            val nextTabLeft = nextTab.left.toFloat()
            val nextTabRight = nextTab.right.toFloat()
            lineLeft =
                mCurrentPositionOffset * nextTabLeft + (1f - mCurrentPositionOffset) * lineLeft
            lineRight =
                mCurrentPositionOffset * nextTabRight + (1f - mCurrentPositionOffset) * lineRight
        }

        return Pair(lineLeft, lineRight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (isPaddingMiddle || mPaddingLeft > 0 || mPaddingRight > 0) {
            val width: Int
            if (isPaddingMiddle) {
                width = getWidth()
            } else {
                // Account for manually set padding for offsetting tab start and end positions.
                width = getWidth() - mPaddingLeft - mPaddingRight
            }

            //Make sure tabContainer is bigger than the HorizontalScrollView to be able to scroll
            mTabsContainer.minimumWidth = width
            //Clipping padding to false to see the tabs while we pass them swiping
            clipToPadding = false
        }

        if (mTabsContainer.childCount > 0) {
            mTabsContainer
                .getChildAt(0)
                .viewTreeObserver
                .addOnGlobalLayoutListener(firstTabGlobalLayoutListener)
        }

        super.onLayout(changed, l, t, r, b)
    }

    private val firstTabGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {

        override fun onGlobalLayout() {
            val view = mTabsContainer.getChildAt(0)
            removeGlobalLayoutListenerJB()

            if (isPaddingMiddle) {
                val mHalfWidthFirstTab = view.width / 2
                mPaddingRight = width / 2 - mHalfWidthFirstTab
                mPaddingLeft = mPaddingRight
            }

            setPadding(mPaddingLeft, paddingTop, mPaddingRight, paddingBottom)
            if (mScrollOffset == 0) mScrollOffset = width / 2 - mPaddingLeft
            mCurrentPosition = mPager!!.currentItem
            mCurrentPositionOffset = 0f
            scrollToChild(mCurrentPosition, 0)
            updateSelection(mCurrentPosition)
        }

        private fun removeGlobalLayoutListenerPreJB() {
            viewTreeObserver.removeGlobalOnLayoutListener(this)
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private fun removeGlobalLayoutListenerJB() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isInEditMode || mTabCount == 0) {
            return
        }

        val height = height
        // draw divider
        if (mDividerWidth > 0) {
            mDividerPaint.strokeWidth = mDividerWidth.toFloat()
            mDividerPaint.color = mDividerColor
            for (i in 0 until mTabCount - 1) {
                val tab = mTabsContainer.getChildAt(i)
                canvas.drawLine(
                    tab.right.toFloat(),
                    mDividerPadding.toFloat(),
                    tab.right.toFloat(),
                    (height - mDividerPadding).toFloat(),
                    mDividerPaint
                )
            }
        }

        // draw underline
        if (mUnderlineHeight > 0) {
            mRectPaint.color = mUnderlineColor
            canvas.drawRect(
                mPaddingLeft.toFloat(),
                (height - mUnderlineHeight).toFloat(),
                (mTabsContainer.width + mPaddingRight).toFloat(),
                height.toFloat(),
                mRectPaint
            )
        }

        // draw indicator line
        if (mIndicatorHeight > 0) {
            mRectPaint.color = mIndicatorColor
            val lines = getIndicatorCoordinates()
            canvas.drawRect(
                lines.first!! + mPaddingLeft,
                (height - mIndicatorHeight).toFloat(),
                lines.second!! + mPaddingLeft,
                height.toFloat(),
                mRectPaint
            )
        }
    }

    fun setOnTabReselectedListener(tabReselectedListener: OnTabReselectedListener) {
        this.mTabReselectedListener = tabReselectedListener
    }

    fun setOnPageChangeListener(listener: ViewPager.OnPageChangeListener) {
        this.mDelegatePageListener = listener
    }

    private inner class PageListener : ViewPager.OnPageChangeListener {

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            mCurrentPosition = position
            mCurrentPositionOffset = positionOffset
            val offset =
                if (mTabCount > 0) (positionOffset * mTabsContainer.getChildAt(position).width).toInt() else 0
            scrollToChild(position, offset)
            invalidate()
            if (mDelegatePageListener != null) {
                mDelegatePageListener?.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(mPager!!.currentItem, 0)
            }
            //Full tabTextAlpha for current item
            val currentTab = mTabsContainer.getChildAt(mPager!!.currentItem)
            select(currentTab)
            //Half transparent for prev item
            if (mPager!!.currentItem - 1 >= 0) {
                val prevTab = mTabsContainer.getChildAt(mPager!!.currentItem - 1)
                unSelect(prevTab)
            }

            //Half transparent for next item
            if (mPager!!.currentItem + 1 <= mPager!!.adapter!!.count - 1) {
                val nextTab = mTabsContainer.getChildAt(mPager!!.currentItem + 1)
                unSelect(nextTab)
            }

            if (mDelegatePageListener != null) {
                mDelegatePageListener?.onPageScrollStateChanged(state)
            }
        }

        override fun onPageSelected(position: Int) {
            updateSelection(position)
            if (mDelegatePageListener != null) {
                mDelegatePageListener?.onPageSelected(position)
            }
        }

    }

    private fun updateSelection(position: Int) {
        for (i in 0 until mTabCount) {
            val tv = mTabsContainer.getChildAt(i)
            val selected = i == position
            if (selected) {
                select(tv)
            } else {
                unSelect(tv)
            }
        }
    }

    private fun unSelect(tab: View?) {
        if (tab != null) {
            val tab_title =
                tab.findViewById(R.id.psts_tab_title) as TextView
            if (tab_title != null) {
                tab_title.isSelected = false
            }
            if (isCustomTabs) (mPager?.adapter as CustomTabProvider).tabUnselected(tab)
        }
    }

    private fun select(tab: View?) {
        if (tab != null) {
            val tab_title =
                tab.findViewById(R.id.psts_tab_title) as TextView
            if (tab_title != null) {
                tab_title.isSelected = true
            }
            if (isCustomTabs) (mPager?.adapter as CustomTabProvider).tabSelected(tab)
        }
    }

    private inner class PagerAdapterObserver : DataSetObserver() {

        var isAttached = false

        override fun onChanged() {
            notifyDataSetChanged()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (mPager != null) {
            if (!mAdapterObserver.isAttached) {
                mPager?.adapter!!.registerDataSetObserver(mAdapterObserver)
                mAdapterObserver.isAttached = true
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mPager != null) {
            if (mAdapterObserver.isAttached) {
                mPager?.adapter!!.unregisterDataSetObserver(mAdapterObserver)
                mAdapterObserver.isAttached = false
            }
        }
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        mCurrentPosition = savedState.currentPosition
        if (mCurrentPosition != 0 && mTabsContainer.childCount > 0) {
            unSelect(mTabsContainer.getChildAt(0))
            select(mTabsContainer.getChildAt(mCurrentPosition))
        }
        requestLayout()
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.currentPosition = mCurrentPosition
        return savedState
    }

    internal class SavedState : BaseSavedState {
        var currentPosition: Int = 0

        constructor(superState: Parcelable) : super(superState)

        private constructor(`in`: Parcel) : super(`in`) {
            currentPosition = `in`.readInt()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(currentPosition)
        }

        companion object {

            @SuppressLint("ParcelCreator")
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }

        override fun describeContents(): Int {
            return 0
        }
    }

    fun getIndicatorColor(): Int {
        return this.mIndicatorColor
    }

    fun getIndicatorHeight(): Int {
        return mIndicatorHeight
    }

    fun getUnderlineColor(): Int {
        return mUnderlineColor
    }

    fun getDividerColor(): Int {
        return mDividerColor
    }

    fun getDividerWidth(): Int {
        return mDividerWidth
    }

    fun getUnderlineHeight(): Int {
        return mUnderlineHeight
    }

    fun getDividerPadding(): Int {
        return mDividerPadding
    }

    fun getScrollOffset(): Int {
        return mScrollOffset
    }

    fun getShouldExpand(): Boolean {
        return isExpandTabs
    }

    fun getTextSize(): Int {
        return mTabTextSize
    }

    fun isTextAllCaps(): Boolean {
        return isTabTextAllCaps
    }

    fun getTextColor(): ColorStateList {
        return mTabTextColor!!
    }

    fun getTabBackground(): Int {
        return mTabBackgroundResId
    }

    fun getTabPaddingLeftRight(): Int {
        return mTabPadding
    }

    fun setIndicatorColor(indicatorColor: Int) {
        this.mIndicatorColor = indicatorColor
        invalidate()
    }

    fun setIndicatorColorResource(resId: Int) {
        this.mIndicatorColor = resources.getColor(resId)
        invalidate()
    }

    fun setIndicatorHeight(indicatorLineHeightPx: Int) {
        this.mIndicatorHeight = indicatorLineHeightPx
        invalidate()
    }

    fun setUnderlineColor(underlineColor: Int) {
        this.mUnderlineColor = underlineColor
        invalidate()
    }

    fun setUnderlineColorResource(resId: Int) {
        this.mUnderlineColor = resources.getColor(resId)
        invalidate()
    }

    fun setDividerColor(dividerColor: Int) {
        this.mDividerColor = dividerColor
        invalidate()
    }

    fun setDividerColorResource(resId: Int) {
        this.mDividerColor = resources.getColor(resId)
        invalidate()
    }

    fun setDividerWidth(dividerWidthPx: Int) {
        this.mDividerWidth = dividerWidthPx
        invalidate()
    }

    fun setUnderlineHeight(underlineHeightPx: Int) {
        this.mUnderlineHeight = underlineHeightPx
        invalidate()
    }

    fun setDividerPadding(dividerPaddingPx: Int) {
        this.mDividerPadding = dividerPaddingPx
        invalidate()
    }

    fun setScrollOffset(scrollOffsetPx: Int) {
        this.mScrollOffset = scrollOffsetPx
        invalidate()
    }

    fun setShouldExpand(shouldExpand: Boolean) {
        this.isExpandTabs = shouldExpand
        if (mPager != null) {
            requestLayout()
        }
    }

    fun setAllCaps(textAllCaps: Boolean) {
        this.isTabTextAllCaps = textAllCaps
    }

    fun setTextSize(textSizePx: Int) {
        this.mTabTextSize = textSizePx
        updateTabStyles()
    }

    fun setTextColorResource(resId: Int) {
        setTextColor(resources.getColor(resId))
    }

    fun setTextColor(textColor: Int) {
        setTextColor(createColorStateList(textColor))
    }

    fun setTextColorStateListResource(resId: Int) {
        setTextColor(resources.getColorStateList(resId))
    }

    fun setTextColor(colorStateList: ColorStateList) {
        this.mTabTextColor = colorStateList
        updateTabStyles()
    }

    private fun createColorStateList(color_state_default: Int): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf() //default
            ),
            intArrayOf(
                color_state_default //default
            )
        )
    }

    private fun createColorStateList(
        color_state_pressed: Int,
        color_state_selected: Int,
        color_state_default: Int
    ): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_pressed), //pressed
                intArrayOf(android.R.attr.state_selected), // enabled
                intArrayOf() //default
            ),
            intArrayOf(color_state_pressed, color_state_selected, color_state_default)
        )
    }

    fun setTypeface(typeface: Typeface, style: Int) {
        this.mTabTextTypeface = typeface
        this.mTabTextTypefaceStyle = style
        updateTabStyles()
    }

    fun setTabBackground(resId: Int) {
        this.mTabBackgroundResId = resId
    }

    fun setTabPaddingLeftRight(paddingPx: Int) {
        this.mTabPadding = paddingPx
        updateTabStyles()
    }

    private fun setWeight(parentWeight: Float, weight: Float) {
        mTabsContainer.weightSum = parentWeight

        val params = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, weight)
        for (i in 0 until mTabCount) {
            val v = mTabsContainer.getChildAt(i)
            val tab_title =
                v.findViewById(R.id.psts_tab_title) as TextView
            tab_title.layoutParams = params
        }
    }

    interface CustomTabProvider {
        fun getCustomTabView(parent: ViewGroup, position: Int): View

        fun tabSelected(tab: View)

        fun tabUnselected(tab: View)
    }

    interface OnTabReselectedListener {
        fun onTabReselected(position: Int)
    }
}