package top.xuqingquan.stack

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import top.xuqingquan.R

/**
 * Created by 许清泉 on 2019-04-21 15:01
 */
class DebugHierarchyViewContainer : ScrollView {
    private var mContext: Context? = null

    private var mLinearLayout: LinearLayout? = null
    private var mTitleLayout: LinearLayout? = null

    private var mItemHeight: Int = 0
    private var mPadding: Int = 0

    private val titleLayout: LinearLayout
        get() {
            if (mTitleLayout != null) {
                return mTitleLayout!!
            }

            mTitleLayout = LinearLayout(mContext)
            mTitleLayout!!.setPadding(dip2px(24f), dip2px(24f), 0, dip2px(8f))
            mTitleLayout!!.orientation = LinearLayout.HORIZONTAL
            val flParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            mTitleLayout!!.layoutParams = flParams

            val title = TextView(mContext)
            title.setText(R.string.scaffold_stack_view)
            title.textSize = 20f
            title.setTextColor(Color.BLACK)
            val p = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            p.gravity = Gravity.CENTER_VERTICAL
            title.layoutParams = p
            mTitleLayout!!.addView(title)
            return mTitleLayout!!
        }

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        mContext = context
        val hScrollView = HorizontalScrollView(context)
        mLinearLayout = LinearLayout(context)
        mLinearLayout!!.orientation = LinearLayout.VERTICAL
        hScrollView.addView(mLinearLayout)
        addView(hScrollView)

        mItemHeight = dip2px(50f)
        mPadding = dip2px(16f)
    }

    private fun dip2px(dp: Float): Int {
        val scale = mContext!!.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    internal fun bindFragmentRecords(fragmentRecords: List<DebugFragmentRecord>?) {
        mLinearLayout!!.removeAllViews()
        val ll = titleLayout
        mLinearLayout!!.addView(ll)

        if (fragmentRecords == null) {
            return
        }

        setView(fragmentRecords, 0, null)
    }

    private fun setView(fragmentRecordList: List<DebugFragmentRecord>, hierarchy: Int, tvItem: TextView?) {
        for (i in fragmentRecordList.indices) {
            val child = fragmentRecordList[i]
            var tempHierarchy = hierarchy

            val childTvItem: TextView
            childTvItem = getTextView(child, tempHierarchy)
            childTvItem.setTag(R.id.scaffold_hierarchy, tempHierarchy)

            val childFragmentRecord = child.childFragmentRecord
            if (childFragmentRecord != null && childFragmentRecord.size > 0) {
                tempHierarchy++
                childTvItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.scaffold_ic_arrow_right, 0, 0, 0)
                val finalChilHierarchy = tempHierarchy
                childTvItem.setOnClickListener { v ->
                    if (v.getTag(R.id.scaffold_isexpand) != null) {
                        val isExpand = v.getTag(R.id.scaffold_isexpand) as Boolean
                        if (isExpand) {
                            childTvItem.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.scaffold_ic_arrow_right,
                                0,
                                0,
                                0
                            )
                            removeView(finalChilHierarchy)
                        } else {
                            handleExpandView(childFragmentRecord, finalChilHierarchy, childTvItem)

                        }
                        v.setTag(R.id.scaffold_isexpand, !isExpand)
                    } else {
                        childTvItem.setTag(R.id.scaffold_isexpand, true)
                        handleExpandView(childFragmentRecord, finalChilHierarchy, childTvItem)
                    }
                }
            } else {
                childTvItem.setPadding(childTvItem.paddingLeft + mPadding, 0, mPadding, 0)
            }

            if (tvItem == null) {
                mLinearLayout!!.addView(childTvItem)
            } else {
                mLinearLayout!!.addView(childTvItem, mLinearLayout!!.indexOfChild(tvItem) + 1)
            }
        }
    }

    private fun handleExpandView(
        childFragmentRecord: List<DebugFragmentRecord>?,
        finalChilHierarchy: Int,
        childTvItem: TextView
    ) {
        setView(childFragmentRecord!!, finalChilHierarchy, childTvItem)
        childTvItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.scaffold_ic_arrow_down, 0, 0, 0)
    }

    private fun removeView(hierarchy: Int) {
        val size = mLinearLayout!!.childCount
        for (i in size - 1 downTo 0) {
            val view = mLinearLayout!!.getChildAt(i)
            if (view.getTag(R.id.scaffold_hierarchy) != null && view.getTag(R.id.scaffold_hierarchy) as Int >= hierarchy) {
                mLinearLayout!!.removeView(view)
            }
        }
    }

    private fun getTextView(fragmentRecord: DebugFragmentRecord, hierarchy: Int): TextView {
        val tvItem = TextView(mContext)

        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mItemHeight)
        tvItem.layoutParams = params
        if (hierarchy == 0) {
            tvItem.setTextColor(Color.parseColor("#333333"))
            tvItem.textSize = 16f
        }
        tvItem.gravity = Gravity.CENTER_VERTICAL
        tvItem.setPadding((mPadding + hierarchy.toDouble() * mPadding.toDouble() * 1.5).toInt(), 0, mPadding, 0)
        tvItem.compoundDrawablePadding = mPadding / 2

        val a = mContext!!.obtainStyledAttributes(intArrayOf(android.R.attr.selectableItemBackground))
        tvItem.background = a.getDrawable(0)
        a.recycle()

        tvItem.text = fragmentRecord.fragmentName

        return tvItem
    }
}