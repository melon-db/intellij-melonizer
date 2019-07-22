package net.seesharpsoft.melon;

import net.seesharpsoft.melon.config.MelonConfig;
import org.junit.Assert;
import org.junit.Test;

public class MelonBuilderUT {
    @Test
    public void should_create_schema() {
        MelonConfig melonConfig = new MelonBuilder()
                .addSchema("schema1")
                .addSchema("schema2")
                .build();
        Assert.assertEquals(2, melonConfig.schemas.size());
        Assert.assertEquals("schema1", melonConfig.schemas.get(0).name);
        Assert.assertEquals("schema2", melonConfig.schemas.get(1).name);
    }

    @Test
    public void should_create_tables() {
        MelonConfig melonConfig = new MelonBuilder()
                .addSchema("schema1")
                .addTable("table1")
                .addTable("table2")
                .addSchema("schema2")
                .addTable("table3")
                .build();
        Assert.assertEquals(2, melonConfig.schemas.size());
        Assert.assertEquals(2, melonConfig.schemas.get(0).tables.size());
        Assert.assertEquals(1, melonConfig.schemas.get(1).tables.size());
        Assert.assertEquals("table1", melonConfig.schemas.get(0).tables.get(0).name);
        Assert.assertEquals("table2", melonConfig.schemas.get(0).tables.get(1).name);
        Assert.assertEquals("table3", melonConfig.schemas.get(1).tables.get(0).name);
    }

    @Test
    public void should_create_columns() {
        MelonConfig melonConfig = new MelonBuilder()
                .addSchema("schema1")
                .addTable("table1")
                .addColumn("column1")
                .primary(true)
                .reference("reference1")
                .source("source1")
                .addColumn("column2")
                .addTable("table2")
                .addSchema("schema2")
                .addTable("table3")
                .addColumn("column3")
                .primary(false)
                .source("source2")
                .build();
        Assert.assertEquals(2, melonConfig.schemas.get(0).tables.get(0).columns.size());
        Assert.assertEquals(0, melonConfig.schemas.get(0).tables.get(1).columns.size());
        Assert.assertEquals(1, melonConfig.schemas.get(1).tables.get(0).columns.size());

        Assert.assertEquals("column1", melonConfig.schemas.get(0).tables.get(0).columns.get(0).name);
        Assert.assertEquals("column2", melonConfig.schemas.get(0).tables.get(0).columns.get(1).name);
        Assert.assertEquals("column3", melonConfig.schemas.get(1).tables.get(0).columns.get(0).name);

        Assert.assertEquals(true, melonConfig.schemas.get(0).tables.get(0).columns.get(0).primary);
        Assert.assertEquals(false, melonConfig.schemas.get(0).tables.get(0).columns.get(1).primary);
        Assert.assertEquals(false, melonConfig.schemas.get(1).tables.get(0).columns.get(0).primary);

        Assert.assertEquals("reference1", melonConfig.schemas.get(0).tables.get(0).columns.get(0).reference);
        Assert.assertEquals(null, melonConfig.schemas.get(0).tables.get(0).columns.get(1).reference);
        Assert.assertEquals(null, melonConfig.schemas.get(1).tables.get(0).columns.get(0).reference);

        Assert.assertEquals("source1", melonConfig.schemas.get(0).tables.get(0).columns.get(0).source);
        Assert.assertEquals(null, melonConfig.schemas.get(0).tables.get(0).columns.get(1).source);
        Assert.assertEquals("source2", melonConfig.schemas.get(1).tables.get(0).columns.get(0).source);
    }
}
