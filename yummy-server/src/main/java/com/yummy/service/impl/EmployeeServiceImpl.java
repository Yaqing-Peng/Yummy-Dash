package com.yummy.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yummy.constant.MessageConstant;
import com.yummy.constant.PasswordConstant;
import com.yummy.constant.StatusConstant;
import com.yummy.context.BaseContext;
import com.yummy.dto.EmployeeDTO;
import com.yummy.dto.EmployeeLoginDTO;
import com.yummy.dto.EmployeePageQueryDTO;
import com.yummy.entity.Employee;
import com.yummy.exception.AccountLockedException;
import com.yummy.exception.AccountNotFoundException;
import com.yummy.exception.PasswordErrorException;
import com.yummy.mapper.EmployeeMapper;
import com.yummy.result.PageResult;
import com.yummy.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * emp login
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1. look up username in db
        Employee employee = employeeMapper.getByUsername(username);

        //2. handle exceptions: null, incorrect password, account locked
        if (employee == null) {
            //null
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //encode password by md5
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //incorrect password
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //account locked
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、return emp object
        return employee;
    }

    /**
     * Add a new Emp
     * @param employeeDTO
     * @return
     */
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //copy object properties
        BeanUtils.copyProperties(employeeDTO, employee);
        //set status
        employee.setStatus(StatusConstant.ENABLE);
        //set password
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        //set create time and update time
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        //set create and update person
//        employee.setCreateUser(BaseContext.getCurrentId());
//        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);
    }

    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);

        long total = page.getTotal();
        List<Employee> records = page.getResult();
        return new PageResult(total, records);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.update(employee);
    }

    @Override
    public Employee getById(Long id) {
        Employee employee = employeeMapper.getById(id);
        employee.setPassword("****");
        return employee;
    }

    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(employee);
    }
}
