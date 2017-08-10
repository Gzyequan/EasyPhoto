package com.yequan.easyphoto.utils.permission;

import java.util.List;

/**
 * Created by Administrator on 2017/8/9 0009.
 */
public interface PermissionListener {
  /**
   * 成功获取权限
   */
  void onGranted();

  /**
   * 为获取权限
   */
  void onDenied(List<String> deniedPermission);
}
