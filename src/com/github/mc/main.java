package com.github.mc;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import static com.github.mc.DaoTool.GetPlayerCount;

public class main extends JavaPlugin {
    public HashMap<String,String[]> GroupInfo = new HashMap<String,String[]>();
    @Override
    public void onEnable(){
        ReloadConfig();
        new DaoTool(getDataFolder()+File.separator+"ChronicleCommands");
        super.onEnable();
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (label.equalsIgnoreCase("JL")){
            if (args.length==1){
                if (args[0].equalsIgnoreCase("list")){
                    Set<String> Groupnames = GroupInfo.keySet();
                    sender.sendMessage("§c§l-----查询信息-----");
                    for (String groupname:Groupnames){
                        sender.sendMessage(groupname);
                    }
                    //查询所有组名字
                }
            }else if (args.length==2){
                if (args[0].equalsIgnoreCase("list")){
                    if (GroupInfo.containsKey(args[1])){
                        String info = DaoTool.GetGroupPlayer(args[1]);
                        String[] split = info.split(",");
                        for (String msg:split){
                            sender.sendMessage("§c§l-----分组:"+args[1]+"-----");
                            sender.sendMessage("§e§l"+msg);
                        }
                    }else {
                        sender.sendMessage("§e§l抱歉,配置项没有这个分组");
                    }
                    //查询一个组
                }
            }else if (args.length==3){
                if (!Bukkit.getOfflinePlayer(args[1]).isOnline()){
                    sender.sendMessage("§e§l玩家不在线");
                    return super.onCommand(sender, command, label, args);
                }

                if (!GroupInfo.containsKey(args[2])){
                    sender.sendMessage("§e§l抱歉,配置项没有这个分组");
                    return super.onCommand(sender, command, label, args);
                }

                if (args[0].equalsIgnoreCase("add")){
                    int Count = GetPlayerCount(args[2], args[1]);
                    if (Count==-1){
                        DaoTool.AddData(args[2],args[1],"1");
                        //首次添加
                    }else {
                        //已经有次数列
                        Count +=1;
                        if (Integer.parseInt(GroupInfo.get(args[2])[0])==Count){
                            //已经达到组次数执行命令
                            String cmd = GroupInfo.get(args[2])[1].replace("{Player}",Bukkit.getPlayer(args[1]).getName());
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),cmd);
                            DaoTool.Updata(args[2],args[1],String.valueOf(Count));
                        }else {
                            DaoTool.Updata(args[2],args[1],String.valueOf(Count));
                            //未达到继续增加1
                        }
                    }
                }else if (args[0].equalsIgnoreCase("info")){
                    int Count = GetPlayerCount(args[2], args[1]);
                    if (Count==-1){
                        DaoTool.AddData(args[2],args[1],"0");
                        //首次添加
                        sender.sendMessage(GroupInfo.get(args[2])[2].replace("{Player}",args[1]).replace("{Count}","0"));
                    }else {
                        sender.sendMessage(GroupInfo.get(args[2])[2].replace("{Player}",args[1]).replace("{Count}",String.valueOf(Count)));
                    }
                }
            }
        }


        return super.onCommand(sender, command, label, args);
    }

    private void ReloadConfig(){
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File file = new File(getDataFolder(),"config.yml");
        if (!(file.exists())){
            saveDefaultConfig();
        }
        Set<String> mines = getConfig().getConfigurationSection("Group").getKeys(false);
        for (String temp:mines){
            String Number = getConfig().getString("Group."+temp+".Number");
            String Cmd = getConfig().getString("Group."+temp+".Cmd");
            String Msg = getConfig().getString("Group."+temp+".Msg");
            GroupInfo.put(temp,new String[]{Number,Cmd,Msg});
        }
    }

    @Override
    public void onDisable(){
        super.onDisable();
    }
}
