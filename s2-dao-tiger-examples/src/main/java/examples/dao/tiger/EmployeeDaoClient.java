/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package examples.dao.tiger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.util.ResourceUtil;

public class EmployeeDaoClient {

    private static final String PATH = "examples/dao/tiger/EmployeeDao.dicon";

    public static void main(String[] args) throws IOException {
        S2Container container = S2ContainerFactory.create(PATH);
        container.init();
        try {
            EmployeeDao dao = (EmployeeDao) container
                    .getComponent(EmployeeDao.class);
            List<?> employees = dao.getAllEmployees();
            for (int i = 0; i < employees.size(); ++i) {
                System.out.println(employees.get(i));
            }

            List<Map<String, Object>> valueLabels = dao.getValueLabel();
            for (Map<String, Object> row : valueLabels) {
                System.out.println(row);
            }

            List<Integer> numbers = dao.getAllEmployeeNumbers();
            for (Integer number : numbers) {
                System.out.println(number);
            }

            Employee employee = dao.getEmployee(7788);
            System.out.println(employee);

            int count = dao.getCount();
            System.out.println("count:" + count);

            dao.getEmployeeByJobDeptno(null, null);
            dao.getEmployeeByJobDeptno("CLERK", null);
            dao.getEmployeeByJobDeptno(null, new Integer(20));
            dao.getEmployeeByJobDeptno("CLERK", new Integer(20));

            System.out.println("updatedRows:" + dao.update(employee));

            EmployeeFileWriter writer = null;
            try {
                String buildPath = ResourceUtil.getBuildDir(
                        EmployeeDaoClient.class).getCanonicalPath();
                writer = new EmployeeFileWriter(buildPath + "/example.csv");
                writer.initialize();
                dao.fetchAllEmployee(writer);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            container.destroy();
        }

    }
}