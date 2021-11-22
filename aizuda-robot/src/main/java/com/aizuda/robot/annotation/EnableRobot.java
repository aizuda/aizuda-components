package com.aizuda.robot.annotation;

import com.aizuda.robot.autoconfigure.RobotAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author LoveHuahua
 * @date 2021年11月21日 23:53
 * @description believe in yourself
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(RobotAutoConfiguration.class)
public @interface EnableRobot {
}
