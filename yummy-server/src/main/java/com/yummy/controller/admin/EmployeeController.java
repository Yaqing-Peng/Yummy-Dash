package com.yummy.controller.admin;

import com.yummy.constant.JwtClaimsConstant;
import com.yummy.dto.EmployeeDTO;
import com.yummy.dto.EmployeeLoginDTO;
import com.yummy.dto.EmployeePageQueryDTO;
import com.yummy.entity.Employee;
import com.yummy.properties.JwtProperties;
import com.yummy.result.PageResult;
import com.yummy.result.Result;
import com.yummy.service.EmployeeService;
import com.yummy.utils.JwtUtil;
import com.yummy.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "Emp related pai")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * Login
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "Emp login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("Employee：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //Login success, generate jwt token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * Logout
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "Emp logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * Add a new Emp
     * @param employeeDTO
     * @return
     */
    @PostMapping
    @ApiOperation("Add a new Emp")
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("add a new emp: {}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("Page query an emp")
    public Result page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("page query an emp:{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("Start or stop an emp account")
    public Result startOrStop(@PathVariable Integer status, Long id){
        log.info("tart or stop an emp account: {},{}", status, id);
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * Get emp by id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("Get emp by id")
    public Result<Employee> getById(@PathVariable Long id){
        log.info("Get emp by id: {}", id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * Update emp
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @ApiOperation("Update emp")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("Update emp: {}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }
}
