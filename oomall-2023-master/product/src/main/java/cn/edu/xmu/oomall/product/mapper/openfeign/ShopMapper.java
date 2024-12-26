//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.mapper.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Shop;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Template;
import com.fasterxml.classmate.TypeBindings;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient("shop-service")
public interface ShopMapper {
    @GetMapping("/shops/{id}")
    InternalReturnObject<Shop> getShopById(@PathVariable Long id);

    @GetMapping("/shops/{shopId}/templates/{id}")
    InternalReturnObject<Template> getTemplateById(@PathVariable Long shopId, @PathVariable Long id);

    // 内部类用于模拟状态保持
    class CallCounter {
        private int count = 0;

        public void increment() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }

    // 调用次数统计器
    CallCounter callCounter = new CallCounter();


    // 调用商铺模块0次的情况
    default InternalReturnObject<Shop> getShopWithoutCallingService(Long id) {
        // 直接返回数据，不调用商铺模块
        return new InternalReturnObject<>(new Shop());
    }


    // 调用商铺模块1次的情况
    default InternalReturnObject<Shop> getShopCallingServiceOnce(@PathVariable Long id) {
        callCounter.increment(); // 更新调用次数
        return getShopById(id);
    }



    // 调用商铺模块2次的情况
    default InternalReturnObject<Template> getTemplateCallingServiceTwice(@PathVariable Long shopId, @PathVariable Long id) {
        callCounter.increment(); // 更新调用次数
        InternalReturnObject<Shop> shop = getShopById(shopId);
        callCounter.increment(); // 更新调用次数
        return getTemplateById(shopId, id);
    }

    // 获取当前调用次数
    default int getCallCount() {
        return callCounter.getCount();
    }

    }
