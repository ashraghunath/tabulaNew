/**
* Copyright (C) 2015, GIAYBAC
*
* Released under the MIT license
*/
package technology.tabula.trap;

/**
 *
 * @author THO Q LUONG Jul 16, 2014 11:19:34 AM
 */
public class TableCell {

    //--------------------------------------------------------------------------
    //  Members
    private final String content;
    private final int idcell;

    //--------------------------------------------------------------------------
    //  Initialization
    public TableCell(int idcell, String content) {
        this.idcell = idcell;
        this.content = content;
    }

    //--------------------------------------------------------------------------
    //  Getter N Setter
    public String getContent() {
        return content;
    }

    public int getIdx() {
        return idcell;
    }
    //--------------------------------------------------------------------------
    //  Method binding
    //--------------------------------------------------------------------------
    //  Implement N Override
    //--------------------------------------------------------------------------
    //  Utils
    //--------------------------------------------------------------------------
    //  Inner class
}
