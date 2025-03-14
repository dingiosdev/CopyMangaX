package com.crow.module_user.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.crow.base.R.id.app_main_fcv
import com.crow.base.app.appContext
import com.crow.base.copymanga.BaseEventEnum
import com.crow.base.copymanga.BaseUser
import com.crow.base.copymanga.entity.Fragments
import com.crow.base.tools.coroutine.FlowBus
import com.crow.base.tools.extensions.doOnClickInterval
import com.crow.base.tools.extensions.navigateToWithBackStack
import com.crow.base.tools.extensions.onCollect
import com.crow.base.tools.extensions.showSnackBar
import com.crow.base.tools.extensions.toast
import com.crow.base.ui.fragment.BaseMviBottomSheetDialogFragment
import com.crow.module_user.R
import com.crow.module_user.databinding.UserFragmentBinding
import com.crow.module_user.ui.adapter.UserRvAdapter
import com.crow.module_user.ui.viewmodel.UserViewModel
import com.google.android.material.R.id.design_bottom_sheet
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.qualifier.named
import com.crow.base.R as baseR

/*************************
 * @Machine: RedmiBook Pro 15 Win11
 * @Path: module_user/src/main/kotlin/com/crow/module_user/ui/fragment
 * @Time: 2023/3/18 21:20
 * @Author: CrowForKotlin
 * @Description: UserRepository
 * @formatter:on
 **************************/

class UserBottomSheetFragment : BaseMviBottomSheetDialogFragment<UserFragmentBinding>() {

    /**
     * ● (Activity 级别)用戶 VM
     *
     * ● 2023-07-02 20:31:08 周日 下午
     */
    private val mUserVM by sharedViewModel<UserViewModel>()

    // 用户适配器数据
    private val mAdapterData = mutableListOf (
        R.drawable.user_ic_usr_24dp to appContext.getString(R.string.user_login),
        R.drawable.user_ic_reg_24dp to appContext.getString(R.string.user_reg),
        R.drawable.user_ic_history_24dp to appContext.getString(R.string.user_browsing_history),
        baseR.drawable.base_ic_download_24dp to appContext.getString(R.string.user_download),
        R.drawable.user_ic_about_24dp to appContext.getString(R.string.user_about),
        R.drawable.user_ic_update_24dp to appContext.getString(R.string.user_check_update),
        R.drawable.user_ic_update_history_24dp to appContext.getString(R.string.user_update_history_title)
    )

    // 用户适配器
    private lateinit var mUserRvAdapter: UserRvAdapter

    override fun getViewBinding(inflater: LayoutInflater) = UserFragmentBinding.inflate(inflater)

    override fun onStart() {
        super.onStart()

        // 设置BottomSheet的 高度
        dialog?.findViewById<View>(design_bottom_sheet)?.layoutParams!!.height = ViewGroup.LayoutParams.MATCH_PARENT
    }

    override fun initView(bundle: Bundle?) {

        mUserRvAdapter = UserRvAdapter(mAdapterData) { pos, content ->

            // 根据 位置 做对应的逻辑处理
            dismissAllowingStateLoss()
            val parentFragment = parentFragmentManager.findFragmentByTag(Fragments.Container.name)!!
            when (pos) {
                // 登录 ＆ 个人信息
                0 -> {
                    if (content == getString(R.string.user_info))
                        parentFragmentManager.navigateToWithBackStack<UserUpdateInfoFragment>(app_main_fcv, parentFragment, null, Fragments.UserInfo.name, Fragments.UserInfo.name)
                    else
                        parentFragmentManager.navigateToWithBackStack<UserLoginFragment>(app_main_fcv, parentFragment, null, Fragments.Login.name, Fragments.Login.name)

                }
                1 -> parentFragmentManager.navigateToWithBackStack<UserRegFragment>(app_main_fcv, parentFragment, null, Fragments.Reg.name, Fragments.Reg.name)
                2 -> toast(getString(baseR.string.BaseStillInDevelopment))
                3 -> toast(getString(baseR.string.BaseStillInDevelopment))
                4 -> parentFragmentManager.navigateToWithBackStack(app_main_fcv, parentFragment, get(named(Fragments.About.name)), Fragments.About.name, Fragments.About.name)
                5 -> FlowBus.with<Unit>(BaseEventEnum.UpdateApp.name).post(lifecycleScope, Unit)
                6 -> parentFragmentManager.navigateToWithBackStack(app_main_fcv, parentFragment, get(named(Fragments.UpdateHistory.name)), Fragments.UpdateHistory.name, Fragments.UpdateHistory.name)
            }
        }

        // 设置 适配器
        mBinding.userRv.adapter = mUserRvAdapter
    }

    override fun initObserver(saveInstanceState: Bundle?) {

        // 用户信息 收集
        mUserVM.userInfo.onCollect(this) {

            // 初始化 Icon链接 设置用户名 退出可见 修改适配器数据
            mUserVM.doLoadIcon(mContext, false) { resource ->  mBinding.userIcon.setImageDrawable(resource) }

            // 数据空 则退出
            if (it == null) return@onCollect

            // 设置昵称
            mBinding.userName.text = getString(R.string.user_nickname, it.mNickname)

            // 退出按钮可见
            mBinding.userExit.visibility = View.VISIBLE

            // 移除适配器首位数据 默认是 登录
            mAdapterData.removeFirst()

            // 索引0插入数据
            mAdapterData.add(0, R.drawable.user_ic_usr_24dp to getString(R.string.user_info))
        }
    }

    override fun initListener() {

        // 点击 头像事件
        mBinding.userIcon.doOnClickInterval {

            // 点击头像 并 深链接跳转
            dismissAllowingStateLoss()

            // 导航至头像Fragment Token不为空则跳转
            parentFragmentManager.navigateToWithBackStack<UserIconFragment>(
                app_main_fcv, this,
                bundleOf("iconUrl" to if (BaseUser.CURRENT_USER_TOKEN.isNotEmpty()) mUserVM.mIconUrl else null),
                Fragments.Icon.name, Fragments.Icon.name
            )
        }

        // 点击 退出事件
        mBinding.userExit.doOnClickInterval {

            // 发送事件清除用户全部数据
            parentFragmentManager.setFragmentResult(BaseEventEnum.LoginCategories.name, bundleOf("isLogout" to true))

            // SnackBar提示
            mBinding.root.showSnackBar(getString(R.string.user_exit_sucess))

            // 关闭当前界面
            dismissAllowingStateLoss()
        }
    }
}