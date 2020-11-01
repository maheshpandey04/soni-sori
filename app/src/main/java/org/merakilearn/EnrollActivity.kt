package org.merakilearn

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.tiagohm.markdownview.css.styles.Github
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.merakilearn.databinding.ActivityDiscoverEnrollBinding
import org.merakilearn.datasource.network.model.Classes
import org.merakilearn.ui.home.HomeViewModel
import org.merakilearn.util.AppUtils
import org.navgurukul.learn.ui.common.toast
import org.navgurukul.learn.ui.common.toolbarColor

class EnrollActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityDiscoverEnrollBinding

    companion object {
        private const val ARG_KEY_CLASS_ID = "arg_class"
        private const val ARG_KEY_IS_ENROLLED = "arg_is_enrolled"

        fun start(
            context: Context,
            classId: Int?,
            isEnrolled: Boolean
        ) {
            val intent = Intent(context, EnrollActivity::class.java)
            intent.putExtra(ARG_KEY_CLASS_ID, classId)
            intent.putExtra(ARG_KEY_IS_ENROLLED, isEnrolled)
            context.startActivity(intent)
        }
    }

    private var classId: Int? = 0
    private var isEnrolled: Boolean = false
    private val viewModel: HomeViewModel by viewModel()
    private var isFromDeepLink = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_discover_enroll)
        if (AppUtils.isUserLoggedIn(this)) {
            parseIntentData()
        } else
            OnBoardingActivity.restartApp(this, OnBoardingActivityArgs(true))

    }

    private fun parseIntentData() {
        if (intent.hasExtra(ARG_KEY_CLASS_ID)) {
            classId = intent.getIntExtra(ARG_KEY_CLASS_ID, 0)
            isEnrolled = intent.getBooleanExtra(ARG_KEY_IS_ENROLLED, false)
        } else {
            isFromDeepLink = true
            val action: String? = intent?.action
            val data: Uri? = intent?.data
            val uriString = data.toString()
            if (action == Intent.ACTION_VIEW) {
                if (uriString.contains("/class/"))
                    classId = uriString.split("/").last().toIntOrNull()

            }
        }
        fetchClassDataAndShow(classId.toString())
    }

    private fun fetchClassDataAndShow(last: String) {
        mBinding.progressBarButton.visibility = View.VISIBLE
        viewModel.fetchClassData(last).observe(this, Observer {
            mBinding.progressBarButton.visibility = View.GONE
            if (it != null) {
                isEnrolled = intent.getBooleanExtra(ARG_KEY_IS_ENROLLED, it.enrolled)
                initPageRender(it)
            } else {
                MainActivity.launch(this)
            }

        })
    }


    private fun initPageRender(classes: Classes) {
        initToolBar()
        initExpandableToolBar(classes)
        initButtonClick(classes)
        initUI(classes)
    }

    private fun initUI(classes: Classes) {
        mBinding.classDetail.tvClassDetail.text = AppUtils.getClassSchedule(classes)
        mBinding.classDetail.tvAbout.text = AppUtils.getAboutClass(classes)
        if (!classes.rules?.en.isNullOrBlank()) {
            mBinding.classDetail.tvSpecialInstruction.apply {
                this.addStyleSheet(Github())
                this.loadMarkdown(classes.rules?.en)
            }
        }
    }

    private fun initButtonClick(classes: Classes) {
        if (isEnrolled) {
            mBinding.enroll.text = getString(R.string.drop_out)
        }
        mBinding.enroll.setOnClickListener {
            mBinding.progressBarButton.visibility = View.VISIBLE
            viewModel.enrollToClass(classes.id, isEnrolled).observe(this, Observer {
                mBinding.progressBarButton.visibility = View.GONE
                if (isEnrolled) {
                    if (it) {
                        toast(getString(R.string.log_out_class))
                        backToPreviousActivity()
                    } else {
                        toast(getString(R.string.unable_to_drop))
                    }
                } else {
                    if (it) {
                        toast(getString(R.string.enrolled))
                        backToPreviousActivity()
                    } else {
                        toast(getString(R.string.unable_to_enroll))
                    }
                }
            })
        }
    }


    private fun initToolBar() {
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    private fun initExpandableToolBar(classes: Classes) {
        mBinding.toolbarLayout.setExpandedTitleColor(toolbarColor())
        mBinding.toolbarLayout.setCollapsedTitleTextColor(toolbarColor())
        mBinding.toolbarLayout.title = classes.title
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            backToPreviousActivity()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        backToPreviousActivity()
    }

    private fun backToPreviousActivity() {
        if (isFromDeepLink) {
            MainActivity.launch(this)
        } else
            finish()
    }

}