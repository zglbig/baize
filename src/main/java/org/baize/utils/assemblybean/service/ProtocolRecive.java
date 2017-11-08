package org.baize.utils.assemblybean.service;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * 作者： 白泽
 * 时间： 2017/11/3.
 * 描述：
 */

public class ProtocolRecive {

    public static Set<ProtocolModule> protocolModules = new HashSet<>();

    public static void protocol(CodeModel model){
        Class c = model.getClazz();
        Constructor[] cs;
        /**
         * 构造函数也是对象
         * java.lang.Constructor封装了构造函数的信息
         *
         */
        cs = c.getConstructors();
        ProtocolModule protocolModule = new ProtocolModule();
        protocolModule.setClazzId(Integer.parseInt(model.getId()));
        List<String> fields = new ArrayList<>();
        for (Constructor constructor : cs) {
            //获取构造函数的参数列表------>得到的是参数列表的类类型
            Class[] paramType = constructor.getParameterTypes();
            int i = 0;
            protocolModule.setClazzName(c.getName());
            for (Class<?> class1 : paramType) {
                i++;
                String type = CheckType.getType(class1,c.getName(),"");
                if(StringUtils.equals(type,"string"))
                    type = "String";
                fields.add(type);
            }
            protocolModule.setField(fields);
        }
        protocolModules.add(protocolModule);
    }
    private static void checkId(){
        Iterator<ProtocolModule> iterator = protocolModules.iterator();
        List<Integer> ids = new ArrayList<>(protocolModules.size());
        while (iterator.hasNext()){
            ProtocolModule module = iterator.next();
            ids.add(module.getClazzId());
        }
        for (int i = 0;i<ids.size()-1;i++){
            if(ids.size()>1)
            for (int j = 1;j<ids.size();j++){
                if(i == j) continue;
                if(ids.get(i) == ids.get(j)){
                    System.err.println("注解为"+ids.get(i)+"的id值重复请重新设值");
                    new RuntimeException("注解为"+ids.get(i)+"的id值重复请重新设值");
                }
            }
        }
    }
    public static void assembly(){
        checkId();
        StringBuffer sb = new StringBuffer();
        sb.append("package org.baize.server.message;\n");
        Iterator<ProtocolModule> iterator = protocolModules.iterator();
        while (iterator.hasNext()){
            ProtocolModule module = iterator.next();
            sb.append("import "+module.getClazzName()+";\n");
        }
        sb.append("public class CommandRecive{\n");
        sb.append("\tprivate static CommandRecive instance;\n");
        sb.append("\tpublic static CommandRecive getInstance(){\n");
        sb.append("\t\tif(instance == null)\n");
        sb.append("\t\t\tinstance = new CommandRecive();\n");
        sb.append("\t\treturn instance;\n");
        sb.append("\t}\n");
        sb.append("\tpublic MessageAb recieve(int id,String[] params){\n");
        sb.append("\t\tswitch (id){\n");
        Iterator<ProtocolModule> iterator1 = protocolModules.iterator();
        while (iterator1.hasNext()){
            ProtocolModule module = iterator1.next();
            sb.append("\t\t\tcase "+module.getClazzId()+":\n");
            sb.append("\t\t\t\treturn get"+StringUtils.substringAfterLast(module.getClazzName(),".")+"(params);\n");
        }
        sb.append("\t\t\tdefault:\n");
        sb.append("\t\t\t\treturn null;\n");
        sb.append("\t\t}\n");
        sb.append("\t}\n");
        Iterator<ProtocolModule> iterator2 = protocolModules.iterator();
        while (iterator2.hasNext()){
            ProtocolModule module = iterator2.next();
            sb.append("\tprivate CommandAb get"+StringUtils.substringAfterLast(module.getClazzName(),".")+"(String[] params){\n");
            int i = 0;
            for (String str:module.getField()){
                i++;
                String type = CheckType.checkProtocolType(str,module.getClazzName());
                if(type.equals("String")){
                    sb.append("\t\t"+type+" value"+i+" = params["+i+"];\n");
                }else if(str.equals("bool")){
                    sb.append("\t\tboolean value"+i+" = "+type+"(params["+i+"]);\n");
                }else {
                    sb.append("\t\t"+str+" value"+i+" = "+type+"(params["+i+"]);\n");
                }
            }
            sb.append("\t\treturn new "+StringUtils.substringAfterLast(module.getClazzName(),".")+"(");
           for (int j = 0;j<module.getField().size();j++){
               if(j<=module.getField().size()-2)
                sb.append("value"+(j+1)+",");
               else
                   sb.append("value"+(j+1));
            }
            sb.append(");\n");
            sb.append("\t}\n");
        }
        sb.append("}");
        WriteFile.writeText("CommandRecive.java",sb.toString(),"D:\\github\\netfromework\\src\\main\\java\\org\\baize\\server\\message");
        System.err.println(sb.toString());
    }
}
