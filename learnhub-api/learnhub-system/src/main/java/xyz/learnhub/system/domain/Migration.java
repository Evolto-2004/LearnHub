package xyz.learnhub.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * @TableName migrations
 */
@TableName(value = "migrations")
public class Migration implements Serializable {
    /** */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** */
    private String migration;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** */
    public Integer getId() {
        return id;
    }

    /** */
    public void setId(Integer id) {
        this.id = id;
    }

    /** */
    public String getMigration() {
        return migration;
    }

    /** */
    public void setMigration(String migration) {
        this.migration = migration;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Migration other = (Migration) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getMigration() == null
                        ? other.getMigration() == null
                        : this.getMigration().equals(other.getMigration()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMigration() == null) ? 0 : getMigration().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", migration=").append(migration);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
